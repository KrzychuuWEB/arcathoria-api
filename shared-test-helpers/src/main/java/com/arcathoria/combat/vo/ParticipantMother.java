package com.arcathoria.combat.vo;

import com.arcathoria.character.vo.Health;

import java.util.UUID;

public final class ParticipantMother {
    static final UUID DEFAULT_ID = UUID.randomUUID();
    static final Health DEFAULT_HEALTH = new Health(100.0, 100.0);

    private UUID uuid = DEFAULT_ID;
    private Health health = DEFAULT_HEALTH;

    private ParticipantMother() {
    }

    public static ParticipantMother aParticipantBuilder() {
        return new ParticipantMother();
    }

    ParticipantMother withId(final UUID id) {
        this.uuid = id;
        return this;
    }

    ParticipantMother withHealth(final Double current, final Double max) {
        this.health = new Health(current, max);
        return this;
    }

    public Participant build() {
        return new Participant(uuid, health);
    }
}
