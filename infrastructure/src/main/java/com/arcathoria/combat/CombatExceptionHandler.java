package com.arcathoria.combat;

import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.combat.exception.CombatApplicationDomainException;
import com.arcathoria.combat.exception.CombatDomainException;
import com.arcathoria.exception.DomainExceptionContract;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice(basePackageClasses = CombatController.class)
@Order(1)
class CombatExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CombatExceptionHandler.class);
    private final ProblemDetailsFactory problemDetailsFactory;

    CombatExceptionHandler(final ProblemDetailsFactory problemDetailsFactory) {
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({CombatDomainException.class, CombatApplicationDomainException.class})
    ProblemDetail handleCombatExceptions(final DomainExceptionContract ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, logger);
    }
}
