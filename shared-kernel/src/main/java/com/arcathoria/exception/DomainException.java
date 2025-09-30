package com.arcathoria.exception;

import java.util.Map;

public abstract class DomainException extends RuntimeException {

    private final String domain;
    private final DomainErrorCode errorCode;
    private final Map<String, Object> context;

    protected DomainException(final String message, final String domain, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message);
        this.domain = domain;
        this.errorCode = errorCode;
        this.context = context;
    }

    public String getDomain() {
        return domain;
    }

    public DomainErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
