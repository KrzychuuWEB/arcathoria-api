package com.arcathoria.character.exception;

import com.arcathoria.character.vo.AccountId;

import java.util.Map;

public class SelectedCharacterNotFoundException extends CharacterApplicationException {

    private final AccountId accountId;

    public SelectedCharacterNotFoundException(final AccountId accountId) {
        super("The selected character was not found for account: " + accountId.value(),
                CharacterExceptionErrorCode.ERR_CHARACTER_SELECTED_NOT_FOUND,
                Map.of("accountId", accountId.value())
        );
        this.accountId = accountId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
