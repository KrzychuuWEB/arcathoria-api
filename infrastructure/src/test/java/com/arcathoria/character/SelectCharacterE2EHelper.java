package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class SelectCharacterE2EHelper {

    private final String baseSelectCharacterUrl = "/characters/selects";
    private TestRestTemplate restTemplate;

    public SelectCharacterE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CharacterDTO selectCharacter(final UUID characterId, final HttpHeaders headers) {
        SelectCharacterDTO selectCharacterDTO = new SelectCharacterDTO(characterId);
        restTemplate.postForEntity(baseSelectCharacterUrl, new HttpEntity<>(selectCharacterDTO, headers), CharacterDTO.class);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CharacterDTO> response = restTemplate.exchange(
                baseSelectCharacterUrl + "/me",
                HttpMethod.GET,
                request,
                CharacterDTO.class
        );

        return response.getBody();
    }
}
