package com.arcathoria.auth;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.RegisterDTO;
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
class AuthControllerIT extends IntegrationTestContainersConfig {

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_authenticate_success_and_return_token() {
        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "secret_password123");
        accountFacade.createNewAccount(registerDTO);
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(registerDTO.email(), registerDTO.password());

        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity("/authenticate", new HttpEntity<>(authRequestDTO), TokenResponseDTO.class);

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

        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity("/authenticate", new HttpEntity<>(authRequestDTO), ApiErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}