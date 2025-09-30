package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatException;
import com.arcathoria.exception.DomainExceptionCodeCategory;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Locale;

import static com.arcathoria.exception.ResponseExceptionBuilder.*;

@RestControllerAdvice
@Order(1)
class CombatExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CombatExceptionHandler.class);
    private final MessageSource messageSource;

    CombatExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CombatException.class)
    ProblemDetail handleCombatExceptions(final CombatException ex, final HttpServletRequest request, final Locale locale) {
        HttpStatus status = mapStatus(ex);

        String detail = messageSource.getMessage(generateKeyWithDots(ex.getDomain(), ex.getErrorCode()),
                ex.getContext().values().toArray(),
                ex.getMessage(),
                locale
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(generateType(ex.getDomain(), ex.getErrorCode())));
        problemDetail.setTitle(generateTitle(ex.getErrorCode()));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("errorCode", ex.getErrorCode().getCodeName());
        problemDetail.setProperty("context", ex.getContext());

        if (status.is4xxClientError()) {
            logger.warn("Domain error: {} {} ctx={}", ex.getDomain(), ex.getErrorCode().getCodeName(), ex.getContext());
        } else {
            logger.error("Domain error: {} {} ctx={}", ex.getDomain(), ex.getErrorCode().getCodeName(), ex.getContext(), ex);
        }

        return problemDetail;
    }

    HttpStatus mapStatus(CombatException ex) {
        return switch (ex.getErrorCode().getCategory()) {
            case DomainExceptionCodeCategory.NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DomainExceptionCodeCategory.CONFLICT -> HttpStatus.CONFLICT;
        };
    }
}
