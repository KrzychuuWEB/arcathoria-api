package com.arcathoria.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DomainException extends RuntimeException implements DomainExceptionContract {

    private final String domain;
    private final DomainErrorCode errorCode;
    private final Map<String, Object> context;

    protected DomainException(final String message, final String domain, final DomainErrorCode errorCode, final Map<String, Object> context) {
        super(message);
        this.domain = domain;
        this.errorCode = errorCode;
        this.context = context == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(context));
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public DomainErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }
}
