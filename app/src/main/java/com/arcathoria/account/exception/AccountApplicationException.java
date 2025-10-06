package com.arcathoria.account.exception;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Map;

public abstract class AccountApplicationException extends AccountDomainException {

    AccountApplicationException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, errorCode, context);
    }
}
