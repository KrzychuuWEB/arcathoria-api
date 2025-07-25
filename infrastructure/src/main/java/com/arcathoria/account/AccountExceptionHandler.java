package com.arcathoria.account;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.exception.EmailExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(AccountExceptionHandler.class);
    private final MessageSource messageSource;

    AccountExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleAccountNotFound(final AccountNotFoundException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Account with id {} not found", ex.getUuid());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("account.get.account.not.found", new Object[]{ex.getUuid()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleEmailExistsException(final EmailExistsException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Email {} has been exists", ex.getEmail());

        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage("account.registration.email.exists", new Object[]{ex.getEmail()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
