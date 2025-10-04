package com.arcathoria.character.exception;

import com.arcathoria.character.vo.AccountId;

import java.util.Map;

public class CharacterNotSelectedException extends CharacterApplicationException {

    private final AccountId accountId;

    public CharacterNotSelectedException(final AccountId accountId) {
        super("Character not selected for account: " + accountId.value(),
                CharacterExceptionErrorCode.ERR_CHARACTER_NOT_SELECTED,
                Map.of("accountId", accountId.value())
        );
        this.accountId = accountId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
