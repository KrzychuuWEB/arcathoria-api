package com.arcathoria.account;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.account.exception.EmailExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Order(1)
class AccountExceptionHandler {

    private final MessageSource messageSource;

    AccountExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleEmailExistsException(EmailExistsException ex, HttpServletRequest request, Locale locale) {
        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage(ex.getMessage(), new Object[]{ex.getEmail()}, locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
