package com.arcathoria.monster.dto;

import java.util.UUID;

public record FileMonsterDTO(UUID monsterId, String monsterName, double currentHealth, double maxHealth) {
}
