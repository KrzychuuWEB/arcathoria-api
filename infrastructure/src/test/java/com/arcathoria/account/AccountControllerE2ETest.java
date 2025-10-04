package com.arcathoria.account;

import com.arcathoria.SetLocaleHelper;
import com.arcathoria.WithPostgres;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.AccountExceptionErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithPostgres
class AccountControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String registerUrl = "/accounts/register";

    @Test
    void should_register_account_and_return_201_status() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("accountRegister@email.com").build();

        ResponseEntity<AccountDTO> response = restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO), AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo(registerDTO.email());
    }

    @Test
    void should_return_EER_ACCOUNT_EMAIL_EXISTS_code_when_email_already_exists() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("takenRegisterAccount@email.com").build();

        restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO), AccountDTO.class);

        HttpHeaders headers = new HttpHeaders();
        SetLocaleHelper.withLocale(headers, "pl");

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO, headers), ProblemDetail.class);
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("już istnieje");
        assertThat(result.getProperties())
                .containsEntry("errorCode", AccountExceptionErrorCode.ERR_ACCOUNT_EMAIL_EXISTS.getCodeName());
    }
}