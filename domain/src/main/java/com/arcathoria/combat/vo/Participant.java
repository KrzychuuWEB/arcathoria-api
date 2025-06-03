package com.arcathoria.combat.vo;

import com.arcathoria.character.vo.Health;

import java.util.Objects;
import java.util.UUID;

public class Participant {

    private final UUID id;
    private final Health health;

    public Participant(final UUID id, final Health health) {
        this.id = id;
        this.health = health;
    }

    public UUID getId() {
        return id;
    }

    public Health getHealth() {
        return health;
    }

    void applyDamage(final double damage) {
        health.subtract(damage);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Participant that = (Participant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
