package com.arcathoria.auth;

import com.arcathoria.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Order(2)
class AuthExceptionHandler {

    private final MessageSource messageSource;

    AuthExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleBadCredentialsException(
            final BadCredentialsException ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("auth.bad.credentials", null, ex.getMessage(), locale),
                "ERR-AUTH-BAD_CREDENTIALS-400",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiErrorResponse handleAccessDeniedException(
            final AccessDeniedException ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                messageSource.getMessage("auth.access.denied", null, ex.getMessage(), locale),
                "ERR-AUTH-FORBIDDEN-403",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiErrorResponse handleExpiredJwtException(
            final ExpiredJwtException ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                messageSource.getMessage("auth.jwt.token.expired", null, ex.getMessage(), locale),
                "ERR-AUTH-EXPIRED_TOKEN-401",
                request.getRequestURI()
        );
    }
}
