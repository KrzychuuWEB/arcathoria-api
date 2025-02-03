package com.arcathoria.account.vo;

import java.util.UUID;

public class AccountId {

    private final UUID value;

    public AccountId(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("Account ID cannot be null.");
        }

        this.value = uuid;
    }

    public UUID getValue() {
        return value;
    }
}
