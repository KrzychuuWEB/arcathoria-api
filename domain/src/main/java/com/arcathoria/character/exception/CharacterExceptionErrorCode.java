package com.arcathoria.character.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

public enum CharacterExceptionErrorCode implements DomainErrorCode {

    ERR_CHARACTER_NOT_SELECTED(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_CHARACTER_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_CHARACTER_OWNER_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_CHARACTER_NAME_EXISTS(DomainExceptionCodeCategory.CONFLICT),
    ERR_SERVICE_UNAVAILABLE(DomainExceptionCodeCategory.SERVICE_UNAVAILABLE),
    ERR_CHARACTER_NOT_OWNED(DomainExceptionCodeCategory.FORBIDDEN);

    private final DomainExceptionCodeCategory category;

    CharacterExceptionErrorCode(final DomainExceptionCodeCategory category) {
        this.category = category;
    }

    @Override
    public DomainExceptionCodeCategory getCategory() {
        return category;
    }

    @Override
    public String getCodeName() {
        return name();
    }
}
