package com.arcathoria.character.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SelectCharacterDTO(
        @NotNull(message = "validation.constraints.NotNull")
        UUID characterId
) {
}
