package com.arcathoria.combat.dto;

import java.util.UUID;

public record ExecuteActionDTO(
        UUID combatId,
        String actionType
) {
}
