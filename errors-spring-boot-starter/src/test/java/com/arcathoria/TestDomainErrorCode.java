package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

enum TestDomainErrorCode implements DomainErrorCode {
    TEST_DOMAIN_ERROR_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND);

    private final DomainExceptionCodeCategory category;

    TestDomainErrorCode(final DomainExceptionCodeCategory category) {
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
