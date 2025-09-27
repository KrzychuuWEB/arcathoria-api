package com.arcathoria.combat;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.CharacterWithAccountContext;
import com.arcathoria.character.SetupCharacterE2EHelper;
import com.arcathoria.combat.dto.CombatIdDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ExecuteActionDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.Damage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CombatControllerE2ETest extends IntegrationTestContainersConfig {

    private final String baseUrl = "/combats";
    private final UUID exampleMonsterId = UUID.fromString("bf4397d8-b4dc-361e-9b6d-191a352e9134");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CombatSessionStoreAdapter combatSessionStoreAdapter;

    private SetupCharacterE2EHelper selectCharacterE2EHelper;
    private CreateCombatE2EHelper createCombatE2EHelper;
    private AccountManagerE2EHelper accountManagerE2EHelper;

    @MockitoSpyBean("meleeMagicDamageStrategy")
    private DamageCalculator spyMeleeCombatActionStrategy;

    @BeforeEach
    void setup() {
        this.selectCharacterE2EHelper = new SetupCharacterE2EHelper(restTemplate);
        this.createCombatE2EHelper = new CreateCombatE2EHelper(restTemplate);
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
    }

    @Test
    void should_init_pve_combat_and_save_combat_in_cache() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        ResponseEntity<CombatResultDTO> response = createCombatE2EHelper.initPveCombat(initPveDTO, context.accountHeaders());
        CombatResultDTO result = response.getBody();

        Optional<CombatSnapshot> snapshotFromStore = combatSessionStoreAdapter.getCombatById(new CombatId(result.combatId()));
        CombatSnapshot snapshot = snapshotFromStore.get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.combatId()).isNotNull();
        assertThat(result.attacker().id()).isEqualTo(context.characterDTO().id());
        assertThat(result.defender().id()).isEqualTo(initPveDTO.monsterId());
        assertThat(result.status()).isEqualTo(CombatStatus.IN_PROGRESS);

        assertThat(snapshot).isNotNull();
        assertThat(snapshot.combatId()).isEqualTo(new CombatId(result.combatId()));
        assertThat(snapshot.combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(snapshot.attacker().participantId().value()).isEqualTo(context.characterDTO().id());
        assertThat(snapshot.defender().participantId().value()).isEqualTo(initPveDTO.monsterId());
    }

    @Test
    void should_return_ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT_code_when_participant_has_active_combat_before_init_new() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        createCombatE2EHelper.initPveCombat(initPveDTO, context.accountHeaders()).getBody();

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, context.accountHeaders()), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT");
    }

    @Test
    void should_return_CombatParticipantUnavailableException_when_monster_not_found() {
        InitPveDTO initPveDTO = new InitPveDTO(UUID.randomUUID());

        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, context.accountHeaders()), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_COMBAT_PARTICIPANT_UNAVAILABLE-400");
    }

    @Test
    void should_return_status_code_CHARACTER_SELECT_NOT_FOUND_when_character_not_selected() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);

        HttpHeaders account = accountManagerE2EHelper.registerAndGetAuthHeaders("not_selected_character_CT@combat.arcathoria");

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, account), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_SELECTED_NOT_FOUND-404");
    }

    @Test
    void should_perform_melee_attack_in_combat_return_new_values_from_combat_store() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, context.accountHeaders()).getBody();

        ExecuteActionDTO executeActionDTO = new ExecuteActionDTO(resultInitCombat.combatId(), ActionType.MELEE.name());
        ResponseEntity<CombatResultDTO> response = restTemplate.postForEntity(baseUrl + "/" + resultInitCombat.combatId() + "/actions/execute", new HttpEntity<>(executeActionDTO, context.accountHeaders()), CombatResultDTO.class);
        CombatResultDTO result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).isNotNull();
        assertThat(result.combatId()).isEqualTo(resultInitCombat.combatId());
        assertThat(result.status()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(result.attacker().currentHp()).isEqualTo(resultInitCombat.attacker().currentHp());
        assertThat(result.defender().currentHp()).isNotEqualTo(resultInitCombat.defender().currentHp());

        Optional<CombatSnapshot> snapshotFromStore = combatSessionStoreAdapter.getCombatById(new CombatId(result.combatId()));
        CombatSnapshot snapshot = snapshotFromStore.get();

        assertThat(snapshot.combatId()).isEqualTo(new CombatId(result.combatId()));
        assertThat(snapshot.combatStatus()).isEqualTo(result.status());
        assertThat(snapshot.attacker().health().getCurrent()).isEqualTo(result.attacker().currentHp());
        assertThat(snapshot.defender().health().getCurrent()).isEqualTo(result.defender().currentHp());
    }

    @Test
    void should_return_finished_combat_when_one_participant_is_dead() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, context.accountHeaders()).getBody();

        Mockito.doReturn(new Damage(resultInitCombat.defender().currentHp()))
                .when(spyMeleeCombatActionStrategy).calculate(Mockito.any());

        ExecuteActionDTO executeActionDTO = new ExecuteActionDTO(resultInitCombat.combatId(), ActionType.MELEE.name());
        ResponseEntity<CombatResultDTO> response = restTemplate.postForEntity(baseUrl + "/" + resultInitCombat.combatId() + "/actions/execute", new HttpEntity<>(executeActionDTO, context.accountHeaders()), CombatResultDTO.class);
        CombatResultDTO result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).isNotNull();
        assertThat(result.combatId()).isEqualTo(resultInitCombat.combatId());
        assertThat(result.status()).isEqualTo(CombatStatus.FINISHED);
        assertThat(result.attacker().currentHp()).isEqualTo(resultInitCombat.attacker().currentHp());
        assertThat(result.defender().currentHp()).isZero();
    }

    @Test
    void should_return_active_combat_for_selected_character_by_account_id() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId);
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, context.accountHeaders()).getBody();

        ResponseEntity<CombatIdDTO> response = restTemplate.exchange(
                baseUrl + "/active",
                HttpMethod.GET,
                new HttpEntity<>(context.accountHeaders()),
                new ParameterizedTypeReference<>() {
                }
        );
        CombatIdDTO result = response.getBody();

        assertThat(result).isNotNull();
        assertThat(resultInitCombat).isNotNull();
        assertThat(result.combatId()).isEqualTo(resultInitCombat.combatId());
    }

    @Test
    void should_return_ParticipantNotHasActiveCombatsException_when_selected_character_not_has_active_combat() {
        CharacterWithAccountContext context = selectCharacterE2EHelper.setupSelectedCharacterWithAccount();

        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                baseUrl + "/active",
                HttpMethod.GET,
                new HttpEntity<>(context.accountHeaders()),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT-404");
    }
}