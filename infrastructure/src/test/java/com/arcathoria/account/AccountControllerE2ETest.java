package com.arcathoria.account;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerE2ETest extends IntegrationTestContainersConfig {

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
    void should_return_409_when_email_already_exists() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("takenRegisterAccount@email.com").build();

        restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO), AccountDTO.class);

        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerDTO), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}