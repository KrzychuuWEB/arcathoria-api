package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class CreateCharacterE2EHelper {

    private final String baseUrl = "/characters";
    private final TestRestTemplate restTemplate;

    public CreateCharacterE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<CharacterDTO> create(final CreateCharacterDTO dto, final HttpHeaders headers) {
        ResponseEntity<CharacterDTO> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(dto, headers), CharacterDTO.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Create character response body is null");
        }

        return response;
    }
}
