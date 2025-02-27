package com.arcathoria.account;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.account.exception.EmailExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
class AccountExceptionHandler {

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleEmailExistsException(EmailExistsException ex, HttpServletRequest request) {
        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
