package com.arcathoria.auth;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.account.RegisterDTOMother;
import com.arcathoria.account.dto.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerE2ETest extends IntegrationTestContainersConfig {

    private String authenticateUrl = "/authenticate";

    @Autowired
    private TestRestTemplate restTemplate;

    private AccountManagerE2EHelper accountManagerE2EHelper;

    @BeforeEach
    void setup() {
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
    }

    @Test
    void should_authenticate_success_and_return_token() {
        RegisterDTO registerDTO = RegisterDTOMother.aRegisterDTO().withEmail("authAccountSuccessAuth@email.com").build();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(registerDTO.email(), registerDTO.password());

        accountManagerE2EHelper.register(registerDTO);

        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), TokenResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String token = response.getBody().token();
        assertThat(token).isNotNull().isNotEmpty();
        String[] tokenParts = token.split("\\.");
        assertThat(tokenParts)
                .hasSize(3)
                .allMatch(part -> !part.isEmpty())
                .allMatch(part -> part.matches("^[A-Za-z0-9-_]+$"));
    }

    @Test
    void should_invalid_credentials_return_bad_request() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("invalid@email.com", "secret_invalid_password123");

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}