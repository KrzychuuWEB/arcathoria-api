package com.arcathoria.account.exception;

import com.arcathoria.ApiException;

public class EmailExistsException extends ApiException {

    public EmailExistsException(final String message) {
        super(message, "ERR_ACCOUNT_EMAIL_EXISTS-409");
    }
}
