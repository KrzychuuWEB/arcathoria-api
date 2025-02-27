package com.arcathoria.auth;

import com.arcathoria.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(2)
class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ApiErrorResponse handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        return new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Bad credentials",
                "ERR-AUTH-UNAUTHORIZED-401",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiErrorResponse handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        return new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Lack of access to resources",
                "ERR-AUTH-FORBIDDEN-403",
                request.getRequestURI()
        );
    }
}
