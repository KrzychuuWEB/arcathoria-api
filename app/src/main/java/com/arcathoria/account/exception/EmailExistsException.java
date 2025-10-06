package com.arcathoria.account.exception;

import com.arcathoria.account.vo.Email;

import java.util.Map;

public class EmailExistsException extends AccountApplicationException {

    private final Email email;

    public EmailExistsException(final Email email) {
        super("Account with email " + email + " is exists",
                AccountExceptionErrorCode.ERR_ACCOUNT_EMAIL_EXISTS,
                Map.of("email", email.value())
        );
        this.email = email;
    }

    public Email getEmail() {
        return email;
    }
}
