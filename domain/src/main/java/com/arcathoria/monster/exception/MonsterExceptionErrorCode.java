package com.arcathoria.monster.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

public enum MonsterExceptionErrorCode implements DomainErrorCode {

    ERR_MONSTER_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND);

    private final DomainExceptionCodeCategory category;

    MonsterExceptionErrorCode(final DomainExceptionCodeCategory category) {
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
