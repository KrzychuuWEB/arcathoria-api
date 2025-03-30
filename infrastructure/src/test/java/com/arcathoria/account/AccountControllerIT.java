package com.arcathoria.account;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.vo.Email;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT extends IntegrationTestContainersConfig {

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "/accounts";

    @Test
    @Transactional
    void should_register_account_and_return_201_status() {
        RegisterDTO registerDTO = new RegisterDTO("account@email.com", "secret_password");

        ResponseEntity<AccountDTO> response = restTemplate.postForEntity(BASE_URL + "/register", new HttpEntity<>(registerDTO), AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo(registerDTO.email());
    }

    @Test
    @Sql(statements = "TRUNCATE TABLE accounts RESTART IDENTITY", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void should_return_409_when_email_already_exists() {
        Email email = new Email("account@email.com");
        RegisterDTO registerDTO = new RegisterDTO(email.value(), "secret_password");

        accountFacade.createNewAccount(registerDTO);

        ResponseEntity<ApiErrorResponse> result = restTemplate.postForEntity(BASE_URL + "/register", new HttpEntity<>(registerDTO), ApiErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}