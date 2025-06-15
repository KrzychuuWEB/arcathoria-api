package com.arcathoria.combat.vo;

import com.arcathoria.character.vo.Health;

import java.util.Objects;
import java.util.UUID;

public class Participant {

    private final UUID id;
    private final Health health;
    private final Attributes attributes;

    public Participant(final UUID id, final Health health, final Attributes attributes) {
        this.id = id;
        this.health = health;
        this.attributes = attributes;
    }

    public UUID getId() {
        return id;
    }

    public Health getHealth() {
        return health;
    }

    public int getIntelligenceLevel() {
        return attributes.intelligence().getLevel();
    }

    public void applyDamage(final double damage) {
        health.subtract(damage);
    }

    public boolean isAlive() {
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
