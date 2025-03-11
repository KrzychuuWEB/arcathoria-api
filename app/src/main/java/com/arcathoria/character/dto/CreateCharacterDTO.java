package com.arcathoria.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCharacterDTO(

        @NotBlank(message = "validation.constraints.NotBlank")
        @Size(min = 3, max = 20, message = "validation.constraints.Size")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "validation.constraints.Pattern"
        )
        String characterName
) {
}
