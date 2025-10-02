package com.arcathoria.monster.exception;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Map;

public abstract class MonsterApplicationException extends MonsterDomainException {

    MonsterApplicationException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, errorCode, context);
    }
}
