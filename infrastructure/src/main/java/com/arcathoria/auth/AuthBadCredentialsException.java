package com.arcathoria.auth;

import com.arcathoria.exception.DomainException;

class AuthBadCredentialsException extends DomainException {

    AuthBadCredentialsException() {
        super("Bad credentials",
                "auth",
                AuthExceptionErrorCode.ERR_AUTH_BAD_CREDENTIALS,
                null
        );
    }
}
