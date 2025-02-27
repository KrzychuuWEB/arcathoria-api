package com.arcathoria;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(9999)
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid input data",
                "ERR-DATA-VALIDATION-400",
                request.getRequestURI()
        );

        ex.getBindingResult().getFieldErrors().forEach(error ->
                response.addDetail(error.getField(), error.getDefaultMessage())
        );

        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                "ERR-JAVA-ILLEGAL_ARGUMENT-400",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiErrorResponse handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        return new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                "ERR-SERVER-500",
                request.getRequestURI()
        );
    }
}
