package com.arcathoria;

public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    public DomainException(final String message, final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
