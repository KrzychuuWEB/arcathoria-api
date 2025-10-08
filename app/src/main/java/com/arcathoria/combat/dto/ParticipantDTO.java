package com.arcathoria.combat.dto;

import java.util.UUID;

public record ParticipantDTO(
        UUID id,
        int currentHp,
        int maxHp
) {
}
