package com.arcathoria.account.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainException;

import java.util.Map;

public abstract class AccountDomainException extends DomainException {

    AccountDomainException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, "account", errorCode, context);
    }
}
