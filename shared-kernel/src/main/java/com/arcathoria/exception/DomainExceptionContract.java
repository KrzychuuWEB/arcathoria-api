package com.arcathoria.exception;

import java.util.Map;

public interface DomainExceptionContract {

    String getDomain();

    DomainErrorCode getErrorCode();

    Map<String, Object> getContext();
}
