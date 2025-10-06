package com.arcathoria.combat.exception;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Map;

public abstract class CombatApplicationException extends CombatDomainException {

    protected CombatApplicationException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, errorCode, context);
    }
}
