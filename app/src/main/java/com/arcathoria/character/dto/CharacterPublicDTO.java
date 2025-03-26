package com.arcathoria.character.dto;

import java.util.UUID;

public record CharacterPublicDTO(
        UUID id,
        String characterName
) {
}
