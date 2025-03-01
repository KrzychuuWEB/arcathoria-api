package com.arcathoria.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "validation.constraints.NotBlank")
        @Email(message = "validation.constraints.Email")
        String email,

        @NotBlank(message = "validation.constraints.NotBlank")
        @Size(min = 8, max = 32, message = "validation.constraints.Size")
        String password
) {
}
