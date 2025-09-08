package com.arcathoria.combat.vo;

public record Damage(int value) {

    public Damage {
        if (value < 0) throw new IllegalArgumentException("Damage cannot be less than 0");
    }
}
