package com.arcathoria.combat.dto;

import com.arcathoria.combat.ParticipantType;

import java.util.UUID;

public record ParticipantView(
        UUID id,
        String name,
        int health,
        int intelligence,
        ParticipantType participantType
) {
}
