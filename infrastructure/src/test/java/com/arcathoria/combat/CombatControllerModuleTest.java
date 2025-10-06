package com.arcathoria.combat;

import com.arcathoria.SetLocaleHelper;
import com.arcathoria.auth.AccountWithAuthenticated;
import com.arcathoria.auth.FakeJwtTokenConfig;
import com.arcathoria.combat.dto.*;
import com.arcathoria.combat.exception.CombatExceptionErrorCode;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.testContainers.WithPostgres;
import com.arcathoria.testContainers.WithRedis;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithRedis
@WithPostgres
@Import({FakeJwtTokenConfig.class, CombatModuleTestConfig.class})
class CombatControllerModuleTest {

    private final String baseUrl = "/combats";
    private final MonsterId exampleMonsterId = new MonsterId(UUID.randomUUID());

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CombatSessionStoreAdapter combatSessionStoreAdapter;

    @Autowired
    private FakeCharacterClient fakeCharacterClient;

    @Autowired
    private FakeMonsterClient fakeMonsterClient;

    @Autowired
    private CreateCombatE2EHelper createCombatE2EHelper;

    @Autowired
    private AccountWithAuthenticated accountWithAuthenticated;

    @AfterEach
    void tearDown() {
        fakeCharacterClient.reset();
        fakeMonsterClient.reset();
    }

    @Test
    void should_init_pve_combat_and_save_combat_in_cache() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        ParticipantView monster = fakeMonsterClient.withDefaultMonster(exampleMonsterId.value()).getMonsterById(exampleMonsterId);
        ParticipantView player = fakeCharacterClient.withDefaultCharacter(accountId.value()).getSelectedCharacterByAccountId(accountId);

        ResponseEntity<CombatResultDTO> response = createCombatE2EHelper.initPveCombat(initPveDTO, headers);
        CombatResultDTO result = response.getBody();

        assertThat(result).isNotNull();
        Optional<CombatSnapshot> snapshotFromStore = combatSessionStoreAdapter.getCombatById(new CombatId(result.combatId()));
        assertThat(snapshotFromStore).isPresent();
        CombatSnapshot snapshot = snapshotFromStore.get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.combatId()).isNotNull();
        assertThat(result.attacker().id()).isEqualTo(player.id());
        assertThat(result.defender().id()).isEqualTo(initPveDTO.monsterId());
        assertThat(result.status()).isEqualTo(CombatStatus.IN_PROGRESS);

        assertThat(snapshot).isNotNull();
        assertThat(snapshot.combatId()).isEqualTo(new CombatId(result.combatId()));
        assertThat(snapshot.combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(snapshot.attacker().participantId().value()).isEqualTo(player.id());
        assertThat(snapshot.attacker().participantType()).isEqualTo(player.participantType());
        assertThat(snapshot.attacker().health().getMax()).isEqualTo(player.health());
        assertThat(snapshot.defender().participantId().value()).isEqualTo(monster.id());
        assertThat(snapshot.defender().participantType()).isEqualTo(monster.participantType());
        assertThat(snapshot.defender().health().getMax()).isEqualTo(monster.health());
    }

    @Test
    void should_return_CombatParticipantNotAvailableException_when_character_not_selected() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value()).throwCharacterNotSelectedException();

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, headers), ProblemDetail.class);
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE.getCodeName());
        assertThat(problem.getDetail()).contains("dostępny");

        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().get("upstream")).isNotNull();
        @SuppressWarnings("unchecked")
        Map<String, Object> upstream = (Map<String, Object>) problem.getProperties().get("upstream");
        assertThat(upstream).containsEntry("type", "character").containsEntry("code", "ERR_CHARACTER_NOT_SELECTED");
    }

    @Test
    void should_return_ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT_code_when_participant_has_active_combat_before_init_new() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        createCombatE2EHelper.initPveCombat(initPveDTO, headers);

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, headers), ProblemDetail.class);
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT.getCodeName());
        assertThat(problem.getDetail()).contains("tylko jedną aktywną walkę");
    }

    @Test
    void should_return_CombatParticipantUnavailableException_when_monster_not_found() {
        InitPveDTO initPveDTO = new InitPveDTO(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value()).withNotFoundException();
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, headers), ProblemDetail.class);
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE.getCodeName());
        assertThat(problem.getDetail()).contains("dostępny");

        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().get("upstream")).isNotNull();
        @SuppressWarnings("unchecked")
        Map<String, Object> upstream = (Map<String, Object>) problem.getProperties().get("upstream");
        assertThat(upstream).containsEntry("type", "monster").containsEntry("code", "ERR_MONSTER_NOT_FOUND");
    }

    @Test
    void should_return_CombatParticipantUnavailableException_when_character_not_selected() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value()).throwCharacterNotSelectedException();

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl + "/init/pve", new HttpEntity<>(initPveDTO, headers), ProblemDetail.class);
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE.getCodeName());

        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getDetail()).contains("dostępny");

        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().get("upstream")).isNotNull();
        @SuppressWarnings("unchecked")
        Map<String, Object> upstream = (Map<String, Object>) problem.getProperties().get("upstream");
        assertThat(upstream).containsEntry("type", "character").containsEntry("code", "ERR_CHARACTER_NOT_SELECTED");
    }

    @Test
    void should_perform_melee_attack_in_combat_return_new_values_from_combat_store() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, headers).getBody();

        assertThat(resultInitCombat).isNotNull();
        ExecuteActionDTO executeActionDTO = new ExecuteActionDTO(resultInitCombat.combatId(), ActionType.MELEE.name());
        ResponseEntity<CombatResultDTO> response = restTemplate.postForEntity(
                baseUrl + "/" + resultInitCombat.combatId() + "/actions/execute", new HttpEntity<>(executeActionDTO, headers), CombatResultDTO.class);
        CombatResultDTO result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).isNotNull();
        assertThat(result.combatId()).isEqualTo(resultInitCombat.combatId());
        assertThat(result.status()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(result.attacker().currentHp()).isEqualTo(resultInitCombat.attacker().currentHp());
        assertThat(result.defender().currentHp()).isNotEqualTo(resultInitCombat.defender().currentHp());

        Optional<CombatSnapshot> snapshotFromStore = combatSessionStoreAdapter.getCombatById(new CombatId(result.combatId()));
        assertThat(snapshotFromStore).isPresent();
        CombatSnapshot snapshot = snapshotFromStore.get();

        assertThat(snapshot.combatId()).isEqualTo(new CombatId(result.combatId()));
        assertThat(snapshot.combatStatus()).isEqualTo(result.status());
        assertThat(snapshot.attacker().health().getCurrent()).isEqualTo(result.attacker().currentHp());
        assertThat(snapshot.defender().health().getCurrent()).isEqualTo(result.defender().currentHp());
    }

    @Test
    void should_return_finished_combat_when_one_participant_is_dead() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeMonsterClient.withCustomMonster(exampleMonsterId.value(), ParticipantViewMother.aParticipant().withHealth(1));
        fakeCharacterClient.withCustomCharacter(accountId.value(), ParticipantViewMother.aParticipant().withHealth(1));

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, headers).getBody();

        assertThat(resultInitCombat).isNotNull();
        ExecuteActionDTO executeActionDTO = new ExecuteActionDTO(resultInitCombat.combatId(), ActionType.MELEE.name());
        ResponseEntity<CombatResultDTO> response =
                restTemplate.postForEntity(baseUrl + "/" + resultInitCombat.combatId() + "/actions/execute", new HttpEntity<>(executeActionDTO, headers), CombatResultDTO.class);
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
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        CombatResultDTO resultInitCombat = createCombatE2EHelper.initPveCombat(initPveDTO, headers).getBody();

        ResponseEntity<CombatIdDTO> response = restTemplate.exchange(
                baseUrl + "/active",
                HttpMethod.GET,
                new HttpEntity<>(headers),
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
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeCharacterClient.withDefaultCharacter(accountId.value());

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                baseUrl + "/active",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT.getCodeName());
        assertThat(problem.getDetail()).contains("nie ma aktywnych walk");
    }

    @Test
    void should_get_combat_by_id_when_id_is_correct_for_selected_character() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        CombatResultDTO saveCombat = createCombatE2EHelper.initPveCombat(initPveDTO, headers).getBody();

        assertThat(saveCombat).isNotNull();
        ResponseEntity<CombatResultDTO> response = restTemplate.exchange(
                baseUrl + "/" + saveCombat.combatId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );
        CombatResultDTO result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).isNotNull();
        assertThat(result).isEqualTo(saveCombat);
    }

    @Test
    void should_return_COMBAT_NOT_FOUND_when_combat_not_found_for_get_combat_by_id() {
        AccountId accountId = new AccountId(UUID.randomUUID());

        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(accountId.value());

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                baseUrl + "/" + UUID.randomUUID(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_NOT_FOUND.getCodeName());
        assertThat(problem.getDetail()).contains("Walka o");
    }

    @Test
    void should_return_ERR_COMBAT_PARTICIPANT_NOT_FOUND_IN_COMBAT_when_combat_not_found_for_get_combat_by_id() {
        InitPveDTO initPveDTO = new InitPveDTO(exampleMonsterId.value());
        AccountId firstAccId = new AccountId(UUID.randomUUID());
        HttpHeaders firstAcc = accountWithAuthenticated.authenticatedUser(firstAccId.value());

        fakeMonsterClient.withDefaultMonster(exampleMonsterId.value());
        fakeCharacterClient.withDefaultCharacter(firstAccId.value());

        CombatResultDTO saveCombat = createCombatE2EHelper.initPveCombat(initPveDTO, firstAcc).getBody();

        AccountId secondAccId = new AccountId(UUID.randomUUID());
        HttpHeaders secondAcc = accountWithAuthenticated.authenticatedUser(secondAccId.value());
        SetLocaleHelper.withLocale(secondAcc, "pl");

        fakeCharacterClient.withDefaultCharacter(secondAccId.value());

        assertThat(saveCombat).isNotNull();
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                baseUrl + "/" + saveCombat.combatId(),
                HttpMethod.GET,
                new HttpEntity<>(secondAcc),
                new ParameterizedTypeReference<>() {
                }
        );
        ProblemDetail problem = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties())
                .containsEntry("errorCode", CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_NOT_FOUND_IN_COMBAT.getCodeName());
        assertThat(problem.getDetail()).contains("nie został znaleziony");
    }
}