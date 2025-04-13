package com.arcathoria;

import com.arcathoria.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(9999)
class GlobalExceptionHandler {

    private final MessageSource messageSource;

    GlobalExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiErrorResponse handleAccessDeniedException(
            final AccessDeniedException ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                messageSource.getMessage("access.denied", null, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationException(final MethodArgumentNotValidException ex, final HttpServletRequest request, final Locale locale) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("validation.error", null, locale),
                "ERR-DATA-VALIDATION-400",
                request.getRequestURI()
        );

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();

            Object[] arguments = error.getArguments();
            String translatedMessage = messageSource.getMessage(error.getDefaultMessage(), arguments, defaultMessage, locale);

            response.addDetail(field, translatedMessage);
        });

        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleConstraintViolationException(final ConstraintViolationException ex, final HttpServletRequest request, final Locale locale) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("validation.error", null, locale),
                "ERR-DATA-VALIDATION-400",
                request.getRequestURI()
        );

        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> messageSource.getMessage(v.getMessage(), new Object[]{}, v.getMessage(), locale)
                ));

        violations.forEach(response::addDetail);
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleIllegalArgumentException(
            final IllegalArgumentException ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("internal.error", null, locale),
                "ERR-JAVA-ILLEGAL_ARGUMENT-400",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiErrorResponse handleAllExceptions(
            final Exception ex, final HttpServletRequest request, final Locale locale) {

        return new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                messageSource.getMessage("internal.error", null, locale),
                "ERR-SERVER-500",
                request.getRequestURI()
        );
    }
}
