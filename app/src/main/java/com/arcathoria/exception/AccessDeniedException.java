package com.arcathoria.exception;

import com.arcathoria.ApiException;

public class AccessDeniedException extends ApiException {

    public AccessDeniedException() {
        super("No access to resource", "ERR_ACCESS_DENIED-403");
    }
}
