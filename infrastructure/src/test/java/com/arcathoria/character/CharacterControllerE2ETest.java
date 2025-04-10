package com.arcathoria.character;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CharacterControllerE2ETest extends IntegrationTestContainersConfig {

    private String randomCharacterName;
    private final String baseUrl = "/characters";
    private final String baseSelectCharacterUrl = "/characters/selects";

    @Autowired
    private TestRestTemplate restTemplate;

    private AccountManagerE2EHelper accountManagerE2EHelper;

    private CreateCharacterE2EHelper createCharacterE2EHelper;

    @BeforeEach
    void setup() {
        this.randomCharacterName = "charName_" + UUIDGenerator.generate(5);
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
        this.createCharacterE2EHelper = new CreateCharacterE2EHelper(restTemplate);
    }

    @Test
    void should_create_new_character_with_valid_data() {
        CreateCharacterDTO createCharacterDTO = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build();

        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("createCharacter@email.com");

        ResponseEntity<CharacterDTO> response = createCharacterE2EHelper.create(createCharacterDTO, headers);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().characterName()).isEqualTo(createCharacterDTO.characterName());
    }

    @Test
    void should_return_CharacterNameExistsException_when_character_name_is_taken() {
        CreateCharacterDTO createCharacterDTO = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName("takeCharacterName").build();
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("createCharacterNameException@email.com");

        createCharacterE2EHelper.create(createCharacterDTO, headers);

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(createCharacterDTO, headers), ApiErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_NAME_EXISTS-409");
    }

    @Test
    void should_return_all_account_characters() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("getAllCharacters@email.com");

        List<CharacterDTO> characters = List.of(
                Objects.requireNonNull(createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody()),
                Objects.requireNonNull(createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName + "1").build(), headers).getBody())
        );

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                baseSelectCharacterUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSameSizeAs(characters);
    }

    @Test
    void should_return_empty_list_if_account_not_have_characters() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("getEmptyListForGetAllCharacters@email.com");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                baseSelectCharacterUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void should_set_character_in_cache_and_return_character_when_data_is_valid() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("setCharacterInCache@email.com");
        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();

        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());

        ResponseEntity<CharacterDTO> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(character);
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_to_select_not_found() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("selectNotFoundException@email.com");
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(UUID.randomUUID());

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_NOT_FOUND-404");
    }

    @Test
    void should_return_AccessDeniedException_when_selected_character_is_not_owned_for_logged_account() {
        HttpHeaders account1 = accountManagerE2EHelper.registerAndGetAuthHeaders("setCharacterAccessDenied1@email.com");
        HttpHeaders account2 = accountManagerE2EHelper.registerAndGetAuthHeaders("setCharacterAccessDenied2@email.com");
        CharacterDTO responseForAccount1 = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(),
                account1
        ).getBody();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(responseForAccount1.id());


        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, account2), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_ACCESS_DENIED-403");
    }

    @Test
    void should_return_selected_character_for_logged_account() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("returnSelectedCharacter@email.com");
        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();

        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());
        restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CharacterDTO> response = restTemplate.exchange(
                baseSelectCharacterUrl + "/me",
                HttpMethod.GET,
                request,
                CharacterDTO.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(character.id());
        assertThat(response.getBody().characterName()).isEqualTo(character.characterName());
    }

    @Test
    void should_return_SelectedCharacterNotFoundException_when_not_selected_character() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("selectedNotFoundException@email.com");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                baseSelectCharacterUrl + "/me",
                HttpMethod.GET,
                request,
                ApiErrorResponse.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_SELECTED_NOT_FOUND-404");
    }

    @Test
    void should_remove_selected_character_when_character_is_select() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("removeSelectedCharacter@email.com");
        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());

        restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, Void.class);

        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(baseSelectCharacterUrl + "/me", HttpMethod.GET, request, ApiErrorResponse.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_remove_selected_character_when_character_is_not_selected() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("removeSelectedCharacterWhenCharacterNotSelected@email.com");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_CHARACTER_SELECTED_NOT_FOUND-404");
    }
}
