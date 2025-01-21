package com.arcathoria.api.player.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePlayerDTO(
        @NotBlank(message = "Username is required.")
        @Size(min = 8, max = 50, message = "Username must be between 3 and 50 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, hyphens, and underscores.")
        String username,

        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter.")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter.")
        @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit.")
        @Pattern(regexp = ".*[@$!%*?&].*", message = "Password must contain at least one special character (@, $, !, %, *, ?, &).")
        String password
) {
}