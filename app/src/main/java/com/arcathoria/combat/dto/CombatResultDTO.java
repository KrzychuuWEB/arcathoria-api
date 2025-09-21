package com.arcathoria.combat.dto;

import com.arcathoria.combat.CombatSide;
import com.arcathoria.combat.CombatStatus;

import java.util.UUID;

public record CombatResultDTO(
        UUID combatId,
        ParticipantDTO attacker,
        ParticipantDTO defender,
        CombatStatus status,
        CombatSide combatSide
) {
}
