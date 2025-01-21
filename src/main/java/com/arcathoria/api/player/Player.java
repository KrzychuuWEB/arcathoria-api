package com.arcathoria.api.player;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    Player() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }


    public static final class PlayerBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private LocalDateTime deletedAt;

        private PlayerBuilder() {
        }

        public static PlayerBuilder aPlayer() {
            return new PlayerBuilder();
        }

        public PlayerBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PlayerBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public PlayerBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public PlayerBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public PlayerBuilder withDeletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Player build() {
            Player player = new Player();
            player.setUsername(username);
            player.setEmail(email);
            player.setPassword(password);
            player.setDeletedAt(deletedAt);
            player.id = this.id;
            return player;
        }
    }
}
