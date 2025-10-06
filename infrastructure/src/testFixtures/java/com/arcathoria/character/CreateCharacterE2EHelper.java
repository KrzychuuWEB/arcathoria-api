package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

class CreateCharacterE2EHelper {

    private final String baseUrl = "/characters";
    private final TestRestTemplate restTemplate;

    CreateCharacterE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseEntity<CharacterDTO> create(final CreateCharacterDTO dto, final HttpHeaders headers) {
        ResponseEntity<CharacterDTO> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(dto, headers), CharacterDTO.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Create character response body is null");
        }

        return response;
    }

    ResponseEntity<ProblemDetail> createResponseProblemDetail(final CreateCharacterDTO dto, final HttpHeaders headers) {
        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(dto, headers), ProblemDetail.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Create character response body is null");
        }

        return response;
    }
}
