package com.arcathoria.player;

import java.time.LocalDateTime;

class Player {

    private Long id;

    private final String username;

    private final String password;

    private final String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    Player(final String username, final String password, final String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    Long getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getEmail() {
        return email;
    }

    LocalDateTime getCreatedAt() {
        return createdAt;
    }

    LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
