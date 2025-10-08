package com.arcathoria.auth;

import com.arcathoria.exception.DomainException;

import java.util.Map;

class ExternalServiceUnavailableException extends DomainException {

    ExternalServiceUnavailableException(final String service) {
        super("Service temporarily unavailable. Please try again later.",
                "auth",
                AuthExceptionErrorCode.ERR_EXTERNAL_SERVICE_UNAVAILABLE,
                Map.of("service", service)
        );
    }
}
