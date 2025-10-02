package com.arcathoria.character.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainException;

import java.util.Map;

public abstract class CharacterDomainException extends DomainException {

    CharacterDomainException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message, "character", errorCode, context);
    }
}
