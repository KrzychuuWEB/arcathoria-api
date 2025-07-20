package com.arcathoria.account.exception;

import com.arcathoria.ApiException;

public class EmailExistsException extends ApiException {

    private final String email;

    public EmailExistsException(final String email) {
        super("Account with email " + email + " is exists", "ERR_ACCOUNT_EMAIL_EXISTS-409");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
