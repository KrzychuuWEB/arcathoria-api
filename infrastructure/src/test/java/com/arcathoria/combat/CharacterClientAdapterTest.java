package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.CreateCharacterDTOMother;
import com.arcathoria.character.CreateCharacterE2EHelper;
import com.arcathoria.character.SelectCharacterE2EHelper;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.arcathoria.account.JwtTokenDecodeHelper.extractAccountId;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CharacterClientAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private CharacterClientAdapter characterClientAdapter;

    @Autowired
    private TestRestTemplate restTemplate;

    private SelectCharacterE2EHelper selectCharacterE2EHelper;
    private CreateCharacterE2EHelper createCharacterE2EHelper;
    private AccountManagerE2EHelper accountManagerE2EHelper;

    @BeforeEach
    void setup() {
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
        this.createCharacterE2EHelper = new CreateCharacterE2EHelper(restTemplate);
        this.selectCharacterE2EHelper = new SelectCharacterE2EHelper(restTemplate);
    }

    @Test
    void should_get_selected_character_by_account_id() {
        CreateCharacterDTO newCharacter = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName("combat" + UUIDGenerator.generate(5)).build();
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("combatCharacterClientGet@email.com");
        ResponseEntity<CharacterDTO> createCharacterResponse = createCharacterE2EHelper.create(newCharacter, headers);
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        UUID accountId = UUID.fromString(extractAccountId(token));
        CharacterDTO characterDTO = selectCharacterE2EHelper.selectCharacter(createCharacterResponse.getBody().id(), headers);

        System.out.println(createCharacterResponse.getBody());
        CharacterDTO result = characterClientAdapter.getSelectedCharacterByAccountId(accountId);

        assertThat(result.id()).isEqualTo(characterDTO.id());
        assertThat(result.characterName()).isEqualTo(characterDTO.characterName());
    }
}