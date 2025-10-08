package com.arcathoria.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "accounts")
class AccountEntity {

    @Id
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
