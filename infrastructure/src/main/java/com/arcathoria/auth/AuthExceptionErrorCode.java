package com.arcathoria.auth;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

enum AuthExceptionErrorCode implements DomainErrorCode {
    ERR_AUTH_BAD_CREDENTIALS(DomainExceptionCodeCategory.UNAUTHORIZED),
    ERR_EXTERNAL_SERVICE_UNAVAILABLE(DomainExceptionCodeCategory.SERVICE_UNAVAILABLE);

    private DomainExceptionCodeCategory category;

    AuthExceptionErrorCode(final DomainExceptionCodeCategory category) {
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
