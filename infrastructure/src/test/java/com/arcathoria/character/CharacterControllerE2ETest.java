package com.arcathoria.character;

import com.arcathoria.SetLocaleHelper;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.WithPostgres;
import com.arcathoria.WithRedis;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import com.arcathoria.character.exception.CharacterExceptionErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithPostgres
@WithRedis
class CharacterControllerE2ETest {

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
        SetLocaleHelper.withLocale(headers, "pl");

        createCharacterE2EHelper.create(createCharacterDTO, headers);

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(createCharacterDTO, headers), ProblemDetail.class);
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Nazwa postaci");
        assertThat(result.getProperties())
                .containsEntry("errorCode", CharacterExceptionErrorCode.ERR_CHARACTER_NAME_EXISTS.getCodeName());
    }

    @Test
    void should_return_all_account_characters() {
        HttpHeaders otherAccount = accountManagerE2EHelper.registerAndGetAuthHeaders("getAll" + UUIDGenerator.generate(5) + "@email.com");
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("getAllCharacters@email.com");

        ResponseEntity<CharacterDTO> createCharacterForOtherAccount = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName + UUIDGenerator.generate(5)).build(), otherAccount);
        ResponseEntity<CharacterDTO> character1 = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName + UUIDGenerator.generate(5)).build(), headers);
        ResponseEntity<CharacterDTO> character2 = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName + UUIDGenerator.generate(5)).build(), headers);

        assertThat(character1.getBody()).isNotNull();
        assertThat(character2.getBody()).isNotNull();

        List<CharacterDTO> characters = List.of(character1.getBody(), character2.getBody());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                baseSelectCharacterUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(createCharacterForOtherAccount.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(character1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(character2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSameSizeAs(characters);
    }

    @Test
    void should_return_empty_list_if_account_not_have_characters() {
        HttpHeaders otherAccount = accountManagerE2EHelper.registerAndGetAuthHeaders("getAllCharacters@email.com");
        createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), otherAccount);

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
        SetLocaleHelper.withLocale(headers, "pl");
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(UUID.randomUUID());

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), ProblemDetail.class);
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać z podanym");
        assertThat(result.getProperties())
                .containsEntry("errorCode", CharacterExceptionErrorCode.ERR_CHARACTER_NOT_FOUND.getCodeName());
    }

    @Test
    void should_return_AccessDeniedException_when_selected_character_is_not_owned_for_logged_account() {
        HttpHeaders account1 = accountManagerE2EHelper.registerAndGetAuthHeaders("setCharacterAccessDenied1@email.com");
        HttpHeaders account2 = accountManagerE2EHelper.registerAndGetAuthHeaders("setCharacterAccessDenied2@email.com");
        SetLocaleHelper.withLocale(account2, "pl");

        CharacterDTO responseForAccount1 = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(),
                account1
        ).getBody();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(responseForAccount1.id());

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, account2), ProblemDetail.class);
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("nie ma dostepu");
        assertThat(result.getProperties())
                .containsEntry("errorCode", CharacterExceptionErrorCode.ERR_CHARACTER_ACCESS_DENIED.getCodeName());
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
        SetLocaleHelper.withLocale(headers, "pl");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                baseSelectCharacterUrl + "/me",
                HttpMethod.GET,
                request,
                ProblemDetail.class
        );
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać nie została wybrana");
        assertThat(result.getProperties())
                .containsEntry("errorCode", CharacterExceptionErrorCode.ERR_CHARACTER_NOT_SELECTED.getCodeName());
    }

    @Test
    void should_remove_selected_character_when_character_is_select() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("removeSelectedCharacter@email.com");
        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());

        restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, Void.class);

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(baseSelectCharacterUrl + "/me", HttpMethod.GET, request, ProblemDetail.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_remove_selected_character_when_character_is_not_selected() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("removeSelectedCharacterWhenCharacterNotSelected@email.com");
        SetLocaleHelper.withLocale(headers, "pl");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, ProblemDetail.class);
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać nie została wybrana");
        assertThat(result.getProperties())
                .containsEntry("errorCode", CharacterExceptionErrorCode.ERR_CHARACTER_NOT_SELECTED.getCodeName());
    }
}
