package com.arcathoria.character.exception;

import java.util.Map;

public class ExternalServiceUnavailableException extends CharacterApplicationException {
    
    public ExternalServiceUnavailableException(final String service) {
        super("Service temporarily unavailable. Please try again later.",
                CharacterExceptionErrorCode.ERR_SERVICE_UNAVAILABLE,
                Map.of("service", service)
        );
    }
}
