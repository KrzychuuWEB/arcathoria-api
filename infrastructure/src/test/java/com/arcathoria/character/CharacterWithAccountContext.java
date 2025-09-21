package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import org.springframework.http.HttpHeaders;

public record CharacterWithAccountContext(
        HttpHeaders accountHeaders,
        CharacterDTO characterDTO
) {
}
