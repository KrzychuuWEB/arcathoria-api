package com.arcathoria;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Order(99)
class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    GlobalExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiProblemDetail handleValidation(final MethodArgumentNotValidException ex,
                                             final HttpServletRequest req,
                                             final Locale locale) {

        var violations = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    Object[] arguments = err.getArguments();
                    String message = messageSource.getMessage(
                            err.getDefaultMessage(),
                            arguments,
                            locale
                    );

                    return Map.of(
                            "field", err.getField(),
                            "message", message,
                            "code", err.getCode(),
                            "rejectedValue", err.getRejectedValue()
                    );
                })
                .toList();

        var pd = problem(HttpStatus.UNPROCESSABLE_ENTITY,
                "urn:arcathoria:common:validation-error",
                "VALIDATION_ERROR",
                messageSource.getMessage("validation.error", null, "Request validation failed.", locale),
                "ERR_COMMON_VALIDATION",
                req);
        pd.setProperty("violations", violations);

        log.warn("Validation error: {}", pd);

        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ApiProblemDetail handleConstraintViolation(final ConstraintViolationException ex,
                                               final HttpServletRequest req,
                                               final Locale locale) {

        var violations = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "message", Objects.requireNonNull(messageSource.getMessage(v.getMessage(), new Object[]{}, v.getMessage(), locale)),
                        "code", v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                        "rejectedValue", v.getInvalidValue()
                ))
                .toList();

        var pd = problem(HttpStatus.UNPROCESSABLE_ENTITY,
                "urn:arcathoria:common:validation-error",
                "VALIDATION_ERROR",
                messageSource.getMessage("validation.error", null, "Request validation failed.", locale),
                "ERR_COMMON_VALIDATION",
                req);
        pd.setProperty("violations", violations);

        log.warn("Constraint violation: {}", pd);

        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ApiProblemDetail handleBadJson(final HttpMessageNotReadableException ex,
                                   final HttpServletRequest req) {
        ex.getMostSpecificCause();
        String detail = ex.getMostSpecificCause().getMessage();
        var pd = problem(HttpStatus.BAD_REQUEST,
                "urn:arcathoria:common:bad-request",
                "BAD_REQUEST",
                detail,
                "ERR_COMMON_BAD_REQUEST",
                req);

        log.warn("Bad request: {}", pd);

        return pd;
    }

    @ExceptionHandler(BindException.class)
    ApiProblemDetail handleBind(final BindException ex,
                                final HttpServletRequest req,
                                final Locale locale) {
        var pd = problem(HttpStatus.BAD_REQUEST,
                "urn:arcathoria:common:bad-request",
                "BAD_REQUEST",
                messageSource.getMessage("validation.error", null, "Request validation failed.", locale),
                "ERR_COMMON_BAD_REQUEST",
                req);

        log.warn("Bad request: {}", pd);

        return pd;
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            IllegalArgumentException.class
    })
    ApiProblemDetail handleParamIssues(final Exception ex,
                                       final HttpServletRequest req,
                                       final Locale locale) {
        String detail = messageSource.getMessage("bad.request", null, ex.getMessage(), locale);
        var pd = problem(HttpStatus.BAD_REQUEST,
                "urn:arcathoria:common:bad-request",
                "BAD_REQUEST",
                detail,
                "ERR_COMMON_BAD_REQUEST",
                req);

        log.warn("Bad request: {}", pd);

        return pd;
    }

    @ExceptionHandler(Throwable.class)
    ApiProblemDetail handleAny(final Throwable ex,
                               final HttpServletRequest req,
                               final Locale locale) {
        String detail = messageSource.getMessage("internal.error", null, "Unexpected server error.", locale);
        var pd = problem(HttpStatus.INTERNAL_SERVER_ERROR,
                "urn:arcathoria:common:internal-error",
                "INTERNAL_SERVER_ERROR",
                detail,
                "ERR_COMMON_INTERNAL",
                req);

        log.error("Server error: {}", pd);

        return pd;
    }

    private ApiProblemDetail problem(final HttpStatus status,
                                     final String type,
                                     final String title,
                                     final String detail,
                                     final String errorCode,
                                     final HttpServletRequest req) {
        ApiProblemDetail pd = new ApiProblemDetail();
        pd.setDetail(detail);
        pd.setStatus(status.value());
        pd.setType(URI.create(type));
        pd.setTitle(title);
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setErrorCode(errorCode);
        return pd;
    }
}
