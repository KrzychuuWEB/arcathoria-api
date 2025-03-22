package com.arcathoria.character;

import com.arcathoria.AccountManagerTest;
import com.arcathoria.ApiErrorResponse;
import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterControllerIT extends PostgreSQLTestContainerConfig {

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
    void should_create_new_character_with_valid_data() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName_cr");

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<CharacterDTO> result = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(dto, headers), CharacterDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isNotNull();
        assertThat(result.getBody().characterName()).isEqualTo(dto.characterName());
    }

    @Test
    @Sql(statements = "TRUNCATE TABLE characters", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void should_create_new_character_return_characterNameExistsException() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName");

        characterFacade.createCharacter(dto, this.accountManagerTest.getId());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(dto, headers), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void should_return_unauthorized_for_create_character() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName");

        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL, new HttpEntity<>(dto), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void should_return_all_account_characters() {
        List<CharacterDTO> characters = List.of(
                characterFacade.createCharacter(new CreateCharacterDTO("all" + UUIDGenerator.generate(15)), accountManagerTest.getId()),
                characterFacade.createCharacter(new CreateCharacterDTO("all" + UUIDGenerator.generate(15)), accountManagerTest.getId())
        );

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                BASE_URL + "/selects",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        List<CharacterDTO> result = response.getBody();

        assertThat(result).hasSameSizeAs(characters);
        assertThat(result).isNotEmpty();
    }

    @Test
    @Sql(statements = "TRUNCATE TABLE characters", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void should_return_empty_list_if_account_not_have_characters() {
        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                BASE_URL + "/selects",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        List<CharacterDTO> result = response.getBody();

        assertThat(result).isEmpty();
    }
}