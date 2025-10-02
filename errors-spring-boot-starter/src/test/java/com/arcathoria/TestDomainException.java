package com.arcathoria;

import com.arcathoria.exception.DomainException;

import java.util.Map;

class TestDomainException extends DomainException {

    TestDomainException(final String message, final Map<String, Object> context) {
        super(message, "test", TestDomainErrorCode.TEST_DOMAIN_ERROR_NOT_FOUND, context);
    }
}
