package com.arcathoria.account.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class AccountNotFoundException extends ApiException {

    private final UUID uuid;

    public AccountNotFoundException(final UUID uuid) {
        super("Account not found for id " + uuid, "ERR_ACCOUNT_NOT_FOUND-404");
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
