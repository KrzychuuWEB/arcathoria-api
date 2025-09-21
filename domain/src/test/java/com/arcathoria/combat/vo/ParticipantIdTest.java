package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipantIdTest {

    @Test
    void should_create_participant_id_when_valid_uuid() {
        UUID uuid = UUID.randomUUID();

        ParticipantId participantId = new ParticipantId(uuid);

        assertEquals(participantId.value(), uuid);
    }
}