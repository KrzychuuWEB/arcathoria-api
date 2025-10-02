package com.arcathoria.account.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

public enum AccountExceptionErrorCode implements DomainErrorCode {

    ERR_ACCOUNT_EMAIL_EXISTS(DomainExceptionCodeCategory.CONFLICT),
    ERR_ACCOUNT_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND);

    private final DomainExceptionCodeCategory category;

    AccountExceptionErrorCode(final DomainExceptionCodeCategory category) {
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
