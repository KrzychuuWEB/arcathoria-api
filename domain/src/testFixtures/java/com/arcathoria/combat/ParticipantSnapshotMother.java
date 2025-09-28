package com.arcathoria.combat;

import com.arcathoria.Gauge;
import com.arcathoria.Level;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.Health;
import com.arcathoria.combat.vo.Intelligence;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

final class ParticipantSnapshotMother {
    static final ParticipantId DEFAULT_ID = new ParticipantId(UUID.randomUUID());
    static final Health DEFAULT_HEALTH = new Health(new Gauge(100, 100));
    static final Intelligence DEFAULT_INTELLIGENCE = new Intelligence(new Level(1));
    static final ParticipantType DEFAULT_TYPE = ParticipantType.PLAYER;

    private ParticipantId uuid = DEFAULT_ID;
    private Health health = DEFAULT_HEALTH;
    private Attributes attributes = new Attributes(DEFAULT_INTELLIGENCE);
    private ParticipantType participantType = DEFAULT_TYPE;

    private ParticipantSnapshotMother() {
    }

    static ParticipantSnapshotMother aParticipantBuilder() {
        return new ParticipantSnapshotMother();
    }

    ParticipantSnapshotMother withId(final ParticipantId id) {
        this.uuid = id;
        return this;
    }

    ParticipantSnapshotMother withHealth(final int current, final int max) {
        this.health = new Health(new Gauge(current, max));
        return this;
    }

    ParticipantSnapshotMother withIntelligence(final Level level) {
        this.attributes = new Attributes(new Intelligence(level));
        return this;
    }

    ParticipantSnapshotMother withParticipantType(final ParticipantType participantType) {
        this.participantType = participantType;
        return this;
    }

    ParticipantSnapshot build() {
        return new ParticipantSnapshot(uuid, health, attributes, participantType);
    }
}
