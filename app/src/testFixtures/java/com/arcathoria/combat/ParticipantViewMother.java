package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;

import java.util.UUID;

class ParticipantViewMother {

    private UUID id;
    private final String name = "exampleName";
    private final int health = 100;
    private final int intelligence = 1;
    private ParticipantType participantType = ParticipantType.PLAYER;

    private ParticipantViewMother() {
    }

    static ParticipantViewMother aParticipantBuilder() {
        return new ParticipantViewMother().withId(UUID.randomUUID());
    }

    ParticipantViewMother withId(final UUID id) {
        this.id = id;
        return this;
    }

    ParticipantViewMother withParticipantType(final ParticipantType participantType) {
        this.participantType = participantType;
        return this;
    }

    ParticipantView build() {
        return new ParticipantView(id, name, health, intelligence, participantType);
    }
}
