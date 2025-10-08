package com.arcathoria.combat.exception;

import java.util.Map;

public class ExternalServiceUnavailableException extends CombatApplicationException {
    
    public ExternalServiceUnavailableException(final String service) {
        super("Service temporarily unavailable. Please try again later.",
                CombatExceptionErrorCode.ERR_SERVICE_UNAVAILABLE,
                Map.of("service", service)
        );
    }
}
