package com.arcathoria.combat.exception;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Map;

public abstract class CombatApplicationDomainException extends CombatDomainException {

    protected CombatApplicationDomainException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, errorCode, context);
    }
}
