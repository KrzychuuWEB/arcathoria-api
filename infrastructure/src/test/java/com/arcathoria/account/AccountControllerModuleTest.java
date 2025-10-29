package com.arcathoria.account;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.SetLocaleHelper;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.AccountExceptionErrorCode;
import com.arcathoria.auth.AccountWithAuthenticated;
import com.arcathoria.auth.FakeJwtTokenConfig;
import com.arcathoria.testContainers.WithPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithPostgres
@Import({FakeJwtTokenConfig.class, AccountModuleTestConfig.class})
class AccountControllerModuleTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountWithAuthenticated accountWithAuthenticated;

    @Autowired
    private CreateAccountE2EHelper createAccountE2EHelper;

    private final String registerUrl = "/accounts/register";
    private final String myAccountUrl = "/accounts/me";

    @Test
    void should_register_account_and_return_201_status() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("accountRegister@arcathoria.com").build();

        ResponseEntity<AccountDTO> response = createAccountE2EHelper.create(registerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo(registerDTO.email());
    }

    @Test
    void should_return_EER_ACCOUNT_EMAIL_EXISTS_code_when_email_already_exists() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("takenRegisterAccount@arcathoria.com").build();

        createAccountE2EHelper.create(registerDTO);

        HttpHeaders headers = new HttpHeaders();
        SetLocaleHelper.withLocale(headers, "pl");

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO, headers), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("już istnieje");
        assertThat(result.getContext()).containsEntry("email", registerDTO.email());
        assertThat(result.getErrorCode()).isEqualTo(AccountExceptionErrorCode.ERR_ACCOUNT_EMAIL_EXISTS.getCodeName());
    }

    @Test
    void should_return_current_account_for_authentication_user() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("getAccountForAuthUser@arcathoria.com").build();
        ResponseEntity<AccountDTO> registeredAccount = createAccountE2EHelper.create(registerDTO);

        assertThat(registeredAccount.getBody()).isNotNull();
        HttpHeaders headersWithAuthAccount = accountWithAuthenticated.authenticatedUser(registeredAccount.getBody().id());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headersWithAuthAccount);
        ResponseEntity<AccountDTO> response = restTemplate.exchange(
                myAccountUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(registeredAccount.getBody().id());
        assertThat(response.getBody().email()).isEqualTo(registeredAccount.getBody().email());
    }

    @Test
    void should_return_ERR_ACCOUNT_NOT_FOUND_when_account_not_found_for_get_by_id() {
        HttpHeaders headersWithAuthAccount = accountWithAuthenticated.authenticatedUser(UUID.randomUUID());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headersWithAuthAccount);
        ResponseEntity<ApiProblemDetail> response = restTemplate.exchange(
                myAccountUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo(AccountExceptionErrorCode.ERR_ACCOUNT_NOT_FOUND.getCodeName());
    }
}