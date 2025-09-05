package com.arcathoria.combat;

import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

public final class ParticipantMother {
    public static final ParticipantId DEFAULT_ID = new ParticipantId(UUID.randomUUID());
    public static final Health DEFAULT_HEALTH = new Health(100.0, 100.0);
    public static final Intelligence DEFAULT_INTELLIGENCE = new Intelligence(1);

    private ParticipantId uuid = DEFAULT_ID;
    private Health health = DEFAULT_HEALTH;
    private Attributes attributes = new Attributes(DEFAULT_INTELLIGENCE);

    private ParticipantMother() {
    }

    public static ParticipantMother aParticipantBuilder() {
        return new ParticipantMother();
    }

    public ParticipantMother withId(final ParticipantId id) {
        this.uuid = id;
        return this;
    }

    public ParticipantMother withHealth(final Double current, final Double max) {
        this.health = new Health(current, max);
        return this;
    }

    public ParticipantMother withIntelligence(final int level) {
        this.attributes = new Attributes(new Intelligence(level));
        return this;
    }

    Participant build() {
        return new Participant(uuid, health, attributes);
    }
}
