package com.arcathoria.exception;

public interface DomainErrorCode {

    DomainExceptionCodeCategory getCategory();

    String getCodeName();
}
