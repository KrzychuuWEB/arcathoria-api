package com.arcathoria.combat.vo;

import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;

import java.util.UUID;

public final class ParticipantMother {
    public static final UUID DEFAULT_ID = UUID.randomUUID();
    public static final Health DEFAULT_HEALTH = new Health(100.0, 100.0);
    public static final Intelligence DEFAULT_INTELLIGENCE = new Intelligence(1);

    private UUID uuid = DEFAULT_ID;
    private Health health = DEFAULT_HEALTH;
    private Attributes attributes = new Attributes(DEFAULT_INTELLIGENCE);

    private ParticipantMother() {
    }

    public static ParticipantMother aParticipantBuilder() {
        return new ParticipantMother();
    }

    public ParticipantMother withId(final UUID id) {
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

    public Participant build() {
        return new Participant(uuid, health, attributes);
    }
}
