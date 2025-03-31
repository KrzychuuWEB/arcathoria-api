package com.arcathoria.exception;

import com.arcathoria.ApiException;

public class AccessDeniedException extends ApiException {

    public AccessDeniedException() {
        super("access.denied", "ERR_ACCESS_DENIED-403");
    }
}
