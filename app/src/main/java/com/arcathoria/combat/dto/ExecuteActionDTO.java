package com.arcathoria.combat.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExecuteActionDTO(
        @NotNull(message = "validation.constraints.NotNull")
        UUID combatId,
        @NotNull(message = "validation.constraints.NotNull")
        String actionType
) {
}
