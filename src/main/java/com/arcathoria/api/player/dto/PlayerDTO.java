package com.arcathoria.api.player.dto;

import java.time.LocalDateTime;

public record PlayerDTO(
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
    
}
