package com.arcathoria.monster.dto;

public record FileMonsterDTO(
        String monsterId,
        String monsterName,
        double currentHealth,
        double maxHealth,
        int intelligence
) {
}
