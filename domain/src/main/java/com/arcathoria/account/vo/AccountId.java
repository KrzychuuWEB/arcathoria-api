package com.arcathoria.account.vo;

import java.util.UUID;

public class AccountId {

    private final UUID value;

    public AccountId(UUID uuid) {
        this.value = uuid;
    }

    public UUID getValue() {
        return value;
    }
}
