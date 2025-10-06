package com.arcathoria.account.exception;

public class AccountBadCredentialsException extends AccountApplicationException {

    public AccountBadCredentialsException() {
        super("Email or password is invalid",
                AccountExceptionErrorCode.ERR_ACCOUNT_BAD_CREDENTIALS,
                null
        );
    }
}
