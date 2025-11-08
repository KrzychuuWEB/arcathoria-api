package com.arcathoria.combat.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InitPveDTO(

        @NotNull(message = "validation.constraints.NotNull")
        UUID monsterId
) {
}
