package com.arcathoria;

import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.auth.AuthRequestDTO;
import com.arcathoria.auth.TokenResponseDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class AccountManagerTest {

    private final TestRestTemplate testRestTemplate;
    private final AccountFacade accountFacade;
    private final String defaultPassword = "secretPassword";
    private UUID id = null;
    private String token = "";

    public AccountManagerTest(final TestRestTemplate testRestTemplate, final AccountFacade accountFacade) {
        this.testRestTemplate = testRestTemplate;
        this.accountFacade = accountFacade;
    }

    public HttpHeaders getAuthorizationHeader(final String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    public void register(final String email) {
        this.id = accountFacade.createNewAccount(new RegisterDTO(email, defaultPassword)).id();
    }

    public void login(final String email) {
        ResponseEntity<TokenResponseDTO> response = testRestTemplate.postForEntity("/authenticate", new HttpEntity<>(
                new AuthRequestDTO(email, defaultPassword)
        ), TokenResponseDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            this.token = response.getBody().token();
        } else {
            throw new RuntimeException("Login failed for test account with email " + email);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
