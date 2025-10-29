package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

class CreateAccountE2EHelper {

    private final TestRestTemplate restTemplate;

    CreateAccountE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseEntity<AccountDTO> create(final RegisterDTO dto) {
        ResponseEntity<AccountDTO> response = restTemplate.postForEntity("/accounts/register", new HttpEntity<>(dto), AccountDTO.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Create account response body is null");
        }

        return response;
    }
}
