package com.arcathoria.auth;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
    private final MessageSource messageSource;
    private final ProblemDetailsFactory problemDetailsFactory;

    AuthExceptionHandler(final MessageSource messageSource, final ProblemDetailsFactory problemDetailsFactory) {
        this.messageSource = messageSource;
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({ExternalServiceUnavailableException.class, AuthBadCredentialsException.class})
    ApiProblemDetail handleAuthExceptions(final DomainException ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, log);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ApiProblemDetail handleBadCredentials(final BadCredentialsException ex,
                                          final HttpServletRequest req,
                                          final Locale locale) {

        String detail = messageSource.getMessage("auth.bad.credentials", null, ex.getMessage(), locale);
        return problem(HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth:bad-credentials",
                "ERR AUTH BAD CREDENTIALS",
                detail,
                "ERR_AUTH_BAD_CREDENTIALS",
                req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ApiProblemDetail handleAccessDenied(final AccessDeniedException ex,
                                        final HttpServletRequest req,
                                        final Locale locale) {

        String detail = messageSource.getMessage("auth.access.denied", null, ex.getMessage(), locale);
        ApiProblemDetail pd = problem(HttpStatus.FORBIDDEN,
                "urn:arcathoria:auth:forbidden",
                "ERR AUTH FORBIDDEN",
                detail,
                "ERR_AUTH_FORBIDDEN",
                req);
        log.warn("Auth access denied: {}", pd);
        return pd;
    }

    @ExceptionHandler(JwtException.class)
    ApiProblemDetail handleExpiredJwt(final JwtException ex,
                                      final HttpServletRequest req,
                                      final Locale locale) {

        String detail = messageSource.getMessage("auth.jwt.token.expired", null, ex.getMessage(), locale);
        return problem(HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth:token-expired",
                "ERR AUTH EXPIRED TOKEN",
                detail,
                "ERR_AUTH_EXPIRED_TOKEN",
                req);
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
