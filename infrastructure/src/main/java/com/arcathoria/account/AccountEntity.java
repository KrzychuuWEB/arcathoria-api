package com.arcathoria.account;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "accounts")
class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String password;

    protected AccountEntity() {
    }

    AccountEntity(final UUID id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    UUID getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }
}
