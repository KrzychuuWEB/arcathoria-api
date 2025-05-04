package com.arcathoria.character.dto;

import java.util.UUID;

public record CharacterDTO(UUID id, String characterName, Double health) {
}
