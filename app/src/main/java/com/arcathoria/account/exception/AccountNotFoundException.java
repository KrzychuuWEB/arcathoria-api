package com.arcathoria.account.exception;

import com.arcathoria.account.vo.AccountId;

import java.util.Map;

public class AccountNotFoundException extends AccountApplicationException {

    private final AccountId accountId;

    public AccountNotFoundException(final AccountId accountId) {
        super("Account not found for id " + accountId,
                AccountExceptionErrorCode.ERR_ACCOUNT_NOT_FOUND,
                Map.of("accountId", accountId.value())
        );
        this.accountId = accountId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
