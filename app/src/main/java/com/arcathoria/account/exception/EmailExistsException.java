package com.arcathoria.account.exception;

import com.arcathoria.ApiException;

public class EmailExistsException extends ApiException {

    private final String email;

    public EmailExistsException(final String email) {
        super("account.registration.email.exists", "ERR_ACCOUNT_EMAIL_EXISTS-409");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
