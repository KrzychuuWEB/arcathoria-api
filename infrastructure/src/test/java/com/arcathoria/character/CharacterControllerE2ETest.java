package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.auth.AuthRequestDTO;
import com.arcathoria.auth.TokenResponseDTO;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CharacterControllerE2ETest extends IntegrationTestContainersConfig {

    private final String baseUrl = "/characters";
    private final String baseSelectCharacterUrl = "/selects";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_create_new_character_with_valid_data() {
        RegisterDTO registerDTO = new RegisterDTO("character@email.com", "secret_password");
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(registerDTO.email(), registerDTO.password());
        CreateCharacterDTO createCharacterDTO = new CreateCharacterDTO("characterName" + UUIDGenerator.generate(5));

        restTemplate.postForEntity("/accounts/register", new HttpEntity<>(registerDTO), AccountDTO.class);
        ResponseEntity<TokenResponseDTO> jwtToken = restTemplate.postForEntity("/authenticate", new HttpEntity<>(authRequestDTO), TokenResponseDTO.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken.getBody().token());
        ResponseEntity<CharacterDTO> result = restTemplate.postForEntity(baseUrl, new HttpEntity<>(createCharacterDTO, headers), CharacterDTO.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isNotNull();
        assertThat(result.getBody().characterName()).isEqualTo(createCharacterDTO.characterName());
    }
}
