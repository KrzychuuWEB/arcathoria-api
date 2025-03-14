package com.arcathoria.character;

import com.arcathoria.AccountManagerTest;
import com.arcathoria.ApiErrorResponse;
import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterControllerTest extends PostgreSQLTestContainerConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CharacterFacade characterFacade;

    @Autowired
    private AccountFacade accountFacade;

    private AccountManagerTest accountManagerTest;

    private final String BASE_URL = "/characters";

    @BeforeAll
    void setUp() {
        String email = "character@email.com";
        this.accountManagerTest = new AccountManagerTest(restTemplate, accountFacade);
        this.accountManagerTest.register(email);
        this.accountManagerTest.login(email);
    }

    @Test
    @Transactional
    void should_create_new_character_with_valid_data() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName");

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<CharacterDTO> result = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(dto, headers), CharacterDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isNotNull();
        assertThat(result.getBody().characterName()).isEqualTo(dto.characterName());
    }

    @Test
    @Sql(statements = "TRUNCATE TABLE characters RESTART IDENTITY", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void should_create_new_character_return_characterNameExistsException() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName");

        characterFacade.createCharacter(dto, this.accountManagerTest.getId());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(dto, headers), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}