package com.arcathoria.combat;

import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.Damage;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.Objects;

class Participant {

    private final ParticipantId id;
    private Health health;
    private final Attributes attributes;

    Participant(final ParticipantId id, final Health health, final Attributes attributes) {
        this.id = id;
        this.health = health;
        this.attributes = attributes;
    }

    ParticipantId getId() {
        return id;
    }

    Health getHealth() {
        return health;
    }

    int getIntelligenceLevel() {
        return attributes.intelligence().level().value();
    }

    void applyDamage(final Damage damage) {
        this.health = health.subtract(damage.value());
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
