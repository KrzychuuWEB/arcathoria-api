package com.arcathoria;

public abstract class ApiException extends RuntimeException {

    private final String errorCode;

    public ApiException(final String message, final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
