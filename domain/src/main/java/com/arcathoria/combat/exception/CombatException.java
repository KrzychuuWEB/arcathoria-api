package com.arcathoria.combat.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainException;

import java.util.Map;

public abstract class CombatException extends DomainException {

    protected CombatException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, "combat", errorCode, context);
    }
}
