package com.arcathoria.character;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.auth.AccountWithAuthenticated;
import com.arcathoria.auth.FakeJwtTokenConfig;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import com.arcathoria.character.exception.CharacterExceptionErrorCode;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.testContainers.WithPostgres;
import com.arcathoria.testContainers.WithRedis;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithPostgres
@WithRedis
@Import({FakeJwtTokenConfig.class, CharacterModuleTestConfig.class})
class CharacterControllerModuleTest {

    private String randomCharacterName;
    private final String baseUrl = "/characters";
    private final String baseSelectCharacterUrl = "/characters/selects";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FakeAccountClient fakeAccountClient;

    @Autowired
    private AccountWithAuthenticated accountWithAuthenticated;

    @Autowired
    private CreateCharacterE2EHelper createCharacterE2EHelper;

    @BeforeEach
    void setup() {
        this.randomCharacterName = "charName_" + UUIDGenerator.generate(5);
    }

    @AfterEach
    void tearDown() {
        fakeAccountClient.reset();
    }

    @Test
    void should_create_new_character_with_valid_data() {
        CreateCharacterDTO createCharacterDTO = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build();

        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());
        fakeAccountClient.withDefaultAccount(accountId.value());

        ResponseEntity<CharacterDTO> response = createCharacterE2EHelper.create(createCharacterDTO, headers);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().characterName()).isEqualTo(createCharacterDTO.characterName());
    }

    @Test
    void should_return_CharacterNameExistsException_when_character_name_is_taken() {
        CreateCharacterDTO createCharacterDTO = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName("takeCharacterName").build();
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        createCharacterE2EHelper.create(createCharacterDTO, headers);

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(createCharacterDTO, headers), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Nazwa postaci");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_NAME_EXISTS.getCodeName());
    }

    @Test
    void should_return_all_account_characters() {
        AccountId otherAccountId = new AccountId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders otherAccount = accountWithAuthenticated.authenticatedUser(otherAccountId.value());
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeAccountClient.withDefaultAccount(otherAccountId.value());
        fakeAccountClient.withDefaultAccount(accountId.value());

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
                baseUrl,
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
        AccountId otherAccountId = new AccountId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders otherAccount = accountWithAuthenticated.authenticatedUser(otherAccountId.value());

        fakeAccountClient.withDefaultAccount(otherAccountId.value());
        fakeAccountClient.withDefaultAccount(accountId.value());

        createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), otherAccount);

        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CharacterDTO>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void should_set_character_in_cache_and_return_character_when_data_is_valid() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();

        assertThat(character).isNotNull();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());

        ResponseEntity<CharacterDTO> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(character);
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_to_select_not_found() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(UUID.randomUUID());

        fakeAccountClient.withDefaultAccount(accountId.value());

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać z podanym");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_NOT_FOUND.getCodeName());
    }

    @Test
    void should_return_AccessDeniedException_when_selected_character_is_not_owned_for_logged_account() {
        AccountId acc1Id = new AccountId(UUID.randomUUID());
        AccountId acc2Id = new AccountId(UUID.randomUUID());
        HttpHeaders account1 = accountWithAuthenticated.authenticatedUser(acc1Id.value());
        HttpHeaders account2 = accountWithAuthenticated.authenticatedWithLangPL(acc2Id.value());

        fakeAccountClient.withDefaultAccount(acc1Id.value());
        fakeAccountClient.withDefaultAccount(acc2Id.value());

        CharacterDTO responseForAccount1 = createCharacterE2EHelper.create(
                CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(),
                account1
        ).getBody();
        assertThat(responseForAccount1).isNotNull();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(responseForAccount1.id());

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, account2), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("nie należy");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_NOT_OWNED.getCodeName());
    }

    @Test
    void should_return_selected_character_for_logged_account() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();

        assertThat(character).isNotNull();
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
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ApiProblemDetail> response = restTemplate.exchange(
                baseSelectCharacterUrl + "/me",
                HttpMethod.GET,
                request,
                ApiProblemDetail.class
        );
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać nie została wybrana");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_NOT_SELECTED.getCodeName());
    }

    @Test
    void should_remove_selected_character_when_character_is_select() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        CharacterDTO character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build(), headers).getBody();
        assertThat(character).isNotNull();
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(character.id());

        restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, Void.class);

        ResponseEntity<ApiProblemDetail> response = restTemplate.exchange(baseSelectCharacterUrl + "/me", HttpMethod.GET, request, ApiProblemDetail.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_remove_selected_character_when_character_is_not_selected() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value());

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ApiProblemDetail> response = restTemplate.exchange(baseSelectCharacterUrl, HttpMethod.DELETE, request, ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("Postać nie została wybrana");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_NOT_SELECTED.getCodeName());
    }

    @Test
    void should_return_CharacterNotOwned_when_account_not_own_character() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(accountId.value());

        fakeAccountClient.withDefaultAccount(accountId.value()).throwCharacterOwnerNotFoundException();

        CreateCharacterDTO createCharacterDTO = CreateCharacterDTOMother.aCreateCharacterDTO().withCharacterName(randomCharacterName).build();
        ResponseEntity<ApiProblemDetail> response = createCharacterE2EHelper.createResponseProblemDetail(createCharacterDTO, headers);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("ERR CHARACTER OWNER NOT FOUND");
        assertThat(result.getDetail()).contains("nie istnieje");
        assertThat(result.getErrorCode()).isEqualTo(CharacterExceptionErrorCode.ERR_CHARACTER_OWNER_NOT_FOUND.getCodeName());
        assertThat(result.getUpstream().service()).isEqualTo("account");
        assertThat(result.getUpstream().code()).isEqualTo("ERR_ACCOUNT_NOT_FOUND");
    }
}
