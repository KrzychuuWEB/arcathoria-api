package com.arcathoria.monster.vo;

public record MonsterName(String value) {

    public MonsterName {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Monster name is required and cannot be empty");
        }
    }
}
