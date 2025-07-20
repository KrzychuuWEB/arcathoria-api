package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.auth.AuthRequestDTO;
import com.arcathoria.auth.TokenResponseDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class AccountManagerE2EHelper {

    private final TestRestTemplate restTemplate;
    private final String registerUrl = "/accounts/register";
    private final String authenticateUrl = "/authenticate";

    public AccountManagerE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<AccountDTO> register(final RegisterDTO registerDTO) {
        ResponseEntity<AccountDTO> registerResponse = restTemplate.postForEntity(
                registerUrl,
                new HttpEntity<>(registerDTO),
                AccountDTO.class
        );

        if (registerResponse.getBody() == null) {
            throw new IllegalStateException("Register response body is null");
        }

        return registerResponse;
    }

    public HttpHeaders registerAndGetAuthHeaders(final String email) {
        String password = "secret_password";

        RegisterDTO registerDTO = new RegisterDTO(email, password);
        ResponseEntity<AccountDTO> registerResponse = restTemplate.postForEntity(
                registerUrl,
                new HttpEntity<>(registerDTO),
                AccountDTO.class
        );

        if (registerResponse.getBody() == null) {
            throw new IllegalStateException("Register response body is null");
        }

        AuthRequestDTO authRequestDTO = new AuthRequestDTO(email, password);
        ResponseEntity<TokenResponseDTO> authResponse = restTemplate.postForEntity(
                authenticateUrl,
                new HttpEntity<>(authRequestDTO),
                TokenResponseDTO.class
        );

        TokenResponseDTO tokenResponse = authResponse.getBody();
        if (tokenResponse == null || tokenResponse.token() == null) {
            throw new IllegalStateException("Authentication failed or token is null");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.token());
        return headers;
    }

}
