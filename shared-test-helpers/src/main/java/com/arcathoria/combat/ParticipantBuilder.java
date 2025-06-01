package com.arcathoria.combat;

import com.arcathoria.character.vo.Health;
import com.arcathoria.monster.vo.Participant;

import java.util.UUID;

class ParticipantBuilder {

    private UUID uuid;
    private Health health;

    static ParticipantBuilder aParticipantBuilder() {
        return new ParticipantBuilder();
    }

    public ParticipantBuilder withId(final UUID id) {
        this.uuid = id;
        return this;
    }

    public ParticipantBuilder withHealth(final Double current, final Double max) {
        this.health = new Health(current, max);
        return this;
    }

    Participant build() {
        return new Participant(uuid, health);
    }
}
