package com.arcathoria.character;

import com.arcathoria.AccountManagerTest;
import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterControllerIT extends IntegrationTestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CharacterFacade characterFacade;

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
    @Sql(statements = "TRUNCATE TABLE characters", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    @Test
    void should_set_character_in_cache_and_return_character_when_data_is_valid() {
        CharacterDTO character = createNewCharacterWithGenerateUniqueValues(this.accountManagerTest.getId());
        SelectCharacterDTO dto = new SelectCharacterDTO(character.id());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<CharacterDTO> result = restTemplate.postForEntity(BASE_URL + "/selects", new HttpEntity<>(dto, headers), CharacterDTO.class);

        assertThat(result.getBody()).isEqualTo(character);
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_to_select_not_found() {
        SelectCharacterDTO dto = new SelectCharacterDTO(UUID.randomUUID());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL + "/selects", new HttpEntity<>(dto, headers), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_return_AccessDeniedException_when_selected_character_is_not_owned_for_logged_account() {
        AccountDTO accountDTO = accountFacade.createNewAccount(new RegisterDTO(UUIDGenerator.generate(15) + "@email.com", "secret_password"));
        CharacterDTO characterDTO = createNewCharacterWithGenerateUniqueValues(accountDTO.id());
        SelectCharacterDTO dto = new SelectCharacterDTO(characterDTO.id());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL + "/selects", new HttpEntity<>(dto, headers), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void should_return_selected_character_for_logged_account() {
        CharacterDTO character = createNewCharacterWithGenerateUniqueValues(accountManagerTest.getId());
        SelectCharacterDTO dto = new SelectCharacterDTO(character.id());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        restTemplate.postForEntity(BASE_URL + "/selects", new HttpEntity<>(dto, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CharacterDTO> result = restTemplate.exchange(
                BASE_URL + "/selects/me",
                HttpMethod.GET,
                request,
                CharacterDTO.class
        );

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isEqualTo(character.id());
        assertThat(result.getBody().characterName()).isEqualTo(character.characterName());
    }

    @Test
    void should_return_not_found_when_not_selected_character() {
        redisTemplate.delete("active-character:" + accountManagerTest.getId());
        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ApiErrorResponse> result = restTemplate.exchange(
                BASE_URL + "/selects/me",
                HttpMethod.GET,
                request,
                ApiErrorResponse.class
        );

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_SELECTED_NOT_FOUND-404");
    }

    @Test
    void should_remove_selected_character_when_character_is_select() {
        CharacterDTO character = createNewCharacterWithGenerateUniqueValues(accountManagerTest.getId());
        SelectCharacterDTO dto = new SelectCharacterDTO(character.id());

        HttpHeaders headers = accountManagerTest.getAuthorizationHeader(accountManagerTest.getToken());
        restTemplate.postForEntity(BASE_URL + "/selects", new HttpEntity<>(dto, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> resultDelete = restTemplate.exchange(BASE_URL + "/selects", HttpMethod.DELETE, request, Void.class);

        ResponseEntity<ApiErrorResponse> result = restTemplate.exchange(BASE_URL + "/selects/me", HttpMethod.GET, request, ApiErrorResponse.class);

        assertThat(resultDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private CharacterDTO createNewCharacterWithGenerateUniqueValues(final UUID accountId) {
        CreateCharacterDTO characterDTO = new CreateCharacterDTO("characterName_" + UUIDGenerator.generate(5));
        return characterFacade.createCharacter(characterDTO, accountId);
    }
}