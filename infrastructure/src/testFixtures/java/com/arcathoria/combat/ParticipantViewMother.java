package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;

import java.util.UUID;

final class ParticipantViewMother {

    private UUID id;
    private String name = "default_name";
    private int health = 100;
    private int intelligence = 1;
    private ParticipantType participantType = ParticipantType.PLAYER;

    private ParticipantViewMother() {
    }

    static ParticipantViewMother aParticipant() {
        return new ParticipantViewMother().withId(UUID.randomUUID());
    }

    ParticipantViewMother withId(final UUID id) {
        this.id = id;
        return this;
    }

    ParticipantViewMother withName(final String name) {
        this.name = name;
        return this;
    }

    ParticipantViewMother withHealth(final int health) {
        this.health = health;
        return this;
    }

    ParticipantViewMother withIntelligence(final int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    ParticipantViewMother withType(final ParticipantType participantType) {
        this.participantType = participantType;
        return this;
    }

    ParticipantView build() {
        return new ParticipantView(id, name, health, intelligence, participantType);
    }
}
