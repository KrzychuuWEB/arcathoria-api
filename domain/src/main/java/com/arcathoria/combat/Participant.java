package com.arcathoria.combat;

import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;

import java.util.Objects;
import java.util.UUID;

class Participant {

    private final UUID id;
    private final Health health;
    private final Attributes attributes;

    Participant(final UUID id, final Health health, final Attributes attributes) {
        this.id = id;
        this.health = health;
        this.attributes = attributes;
    }

    UUID getId() {
        return id;
    }

    Health getHealth() {
        return health;
    }

    int getIntelligenceLevel() {
        return attributes.intelligence().getLevel();
    }

    void applyDamage(final double damage) {
        health.subtract(damage);
    }

    boolean isAlive() {
        return health.isAlive();
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
