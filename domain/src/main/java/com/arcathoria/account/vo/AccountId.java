package com.arcathoria.account.vo;

import java.util.UUID;

public class AccountId {

    private final UUID id;

    public AccountId(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("Account ID cannot be null.");
        }

        this.id = uuid;
    }

    public UUID getId() {
        return id;
    }
}
