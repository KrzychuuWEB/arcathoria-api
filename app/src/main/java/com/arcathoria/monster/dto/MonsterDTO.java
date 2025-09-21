package com.arcathoria.monster.dto;

import java.util.UUID;

public record MonsterDTO(UUID id, String name, Integer currentHealth, Integer maxHealth, Integer intelligence) {
}
