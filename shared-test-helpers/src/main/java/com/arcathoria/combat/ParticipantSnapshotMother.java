package com.arcathoria.combat;

import com.arcathoria.character.vo.Gauge;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.character.vo.Level;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

public final class ParticipantSnapshotMother {
    public static final ParticipantId DEFAULT_ID = new ParticipantId(UUID.randomUUID());
    public static final Health DEFAULT_HEALTH = new Health(new Gauge(100, 100));
    public static final Intelligence DEFAULT_INTELLIGENCE = new Intelligence(new Level(1));
    public static final ParticipantType DEFAULT_TYPE = ParticipantType.PLAYER;

    private ParticipantId uuid = DEFAULT_ID;
    private Health health = DEFAULT_HEALTH;
    private Attributes attributes = new Attributes(DEFAULT_INTELLIGENCE);
    private ParticipantType participantType = DEFAULT_TYPE;

    private ParticipantSnapshotMother() {
    }

    public static ParticipantSnapshotMother aParticipantBuilder() {
        return new ParticipantSnapshotMother();
    }

    public ParticipantSnapshotMother withId(final ParticipantId id) {
        this.uuid = id;
        return this;
    }

    public ParticipantSnapshotMother withHealth(final int current, final int max) {
        this.health = new Health(new Gauge(current, max));
        return this;
    }

    public ParticipantSnapshotMother withIntelligence(final Level level) {
        this.attributes = new Attributes(new Intelligence(level));
        return this;
    }

    public ParticipantSnapshotMother withParticipantType(final ParticipantType participantType) {
        this.participantType = participantType;
        return this;
    }

    ParticipantSnapshot build() {
        return new ParticipantSnapshot(uuid, health, attributes, participantType);
    }
}
