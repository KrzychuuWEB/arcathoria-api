package com.arcathoria.auth;

import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Locale;

@RestControllerAdvice(basePackageClasses = AuthController.class)
@Order(1)
class AuthExceptionHandler {

    private static final Logger log = LogManager.getLogger(AuthExceptionHandler.class);
    private static final String CODE_FIELD = "errorCode";
    private final MessageSource messageSource;
    private final ProblemDetailsFactory problemDetailsFactory;

    AuthExceptionHandler(final MessageSource messageSource, final ProblemDetailsFactory problemDetailsFactory) {
        this.messageSource = messageSource;
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({ExternalServiceUnavailableException.class, AuthBadCredentialsException.class})
    ProblemDetail handleAuthExceptions(final DomainException ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, log);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail handleBadCredentials(final BadCredentialsException ex,
                                       final HttpServletRequest req,
                                       final Locale locale) {

        String detail = messageSource.getMessage("auth.bad.credentials", null, ex.getMessage(), locale);
        ProblemDetail pd = problem(HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth:bad-credentials",
                "BAD_CREDENTIALS",
                detail,
                req);
        pd.setProperty(CODE_FIELD, "ERR_AUTH_BAD_CREDENTIALS");
        return pd;
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDenied(final AccessDeniedException ex,
                                     final HttpServletRequest req,
                                     final Locale locale) {

        String detail = messageSource.getMessage("auth.access.denied", null, ex.getMessage(), locale);
        ProblemDetail pd = problem(HttpStatus.FORBIDDEN,
                "urn:arcathoria:auth:forbidden",
                "FORBIDDEN",
                detail,
                req);
        pd.setProperty(CODE_FIELD, "ERR_AUTH_FORBIDDEN");
        log.warn("Auth access denied: {}", pd);
        return pd;
    }

    @ExceptionHandler(JwtException.class)
    ProblemDetail handleExpiredJwt(final JwtException ex,
                                   final HttpServletRequest req,
                                   final Locale locale) {

        String detail = messageSource.getMessage("auth.jwt.token.expired", null, ex.getMessage(), locale);
        ProblemDetail pd = problem(HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth:token-expired",
                "TOKEN_EXPIRED",
                detail,
                req);
        pd.setProperty(CODE_FIELD, "ERR_AUTH_EXPIRED_TOKEN");
        return pd;
    }

    private ProblemDetail problem(final HttpStatus status,
                                  final String type,
                                  final String title,
                                  final String detail,
                                  final HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setType(URI.create(type));
        pd.setTitle(title);
        pd.setInstance(URI.create(req.getRequestURI()));
        return pd;
    }
}
