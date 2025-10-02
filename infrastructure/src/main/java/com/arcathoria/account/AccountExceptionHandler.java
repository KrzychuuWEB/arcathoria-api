package com.arcathoria.account;

import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.account.exception.AccountApplicationException;
import com.arcathoria.account.exception.AccountDomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Order(1)
class AccountExceptionHandler {

    private static final Logger logger = LogManager.getLogger(AccountExceptionHandler.class);
    private final ProblemDetailsFactory problemDetailsFactory;

    AccountExceptionHandler(final ProblemDetailsFactory problemDetailsFactory) {
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({AccountDomainException.class, AccountApplicationException.class})
    ProblemDetail handleAccountExceptions(final AccountDomainException ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, logger);
    }
}
