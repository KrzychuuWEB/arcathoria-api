package com.arcathoria.combat.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CombatIdDTO(
        @NotNull(message = "validation.constraints.NotNull")
        UUID combatId
) {
}
