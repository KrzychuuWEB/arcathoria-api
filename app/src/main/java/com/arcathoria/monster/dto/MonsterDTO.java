package com.arcathoria.monster.dto;

import java.util.UUID;

public record MonsterDTO(UUID id, String name, Double currentHealth, Double maxHealth) {
}
