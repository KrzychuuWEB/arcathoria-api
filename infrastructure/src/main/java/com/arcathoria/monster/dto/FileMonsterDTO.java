package com.arcathoria.monster.dto;

import java.util.UUID;

public record FileMonsterDTO(UUID monsterId, String monsterName, int currentHealth, int maxHealth,
                             int intelligence) {
}
