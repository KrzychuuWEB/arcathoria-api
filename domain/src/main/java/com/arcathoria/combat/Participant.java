package com.arcathoria.combat;

import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.Damage;
import com.arcathoria.combat.vo.Health;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.Objects;

class Participant {

    static Participant restore(final ParticipantSnapshot snapshot) {
        return new Participant(
                snapshot.participantId(),
                snapshot.health(),
                snapshot.attributes(),
                snapshot.participantType()
        );
    }

    private final ParticipantId id;
    private Health health;
    private final Attributes attributes;
    private final ParticipantType participantType;

    private Participant(final ParticipantId id, final Health health, final Attributes attributes, final ParticipantType participantType) {
        this.id = id;
        this.health = health;
        this.attributes = attributes;
        this.participantType = participantType;
    }

    ParticipantSnapshot getSnapshot() {
        return new ParticipantSnapshot(
                id,
                health,
                attributes,
                participantType
        );
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
