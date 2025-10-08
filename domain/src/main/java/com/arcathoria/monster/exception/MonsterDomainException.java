package com.arcathoria.monster.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainException;

import java.util.Map;

public abstract class MonsterDomainException extends DomainException {

    MonsterDomainException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, "monster", errorCode, context);
    }
}
