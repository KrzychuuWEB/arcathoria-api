package com.arcathoria.character.exception;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Map;

public abstract class CharacterApplicationException extends CharacterDomainException {

    CharacterApplicationException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, errorCode, context);
    }
}
