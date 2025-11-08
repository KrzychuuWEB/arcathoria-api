package com.arcathoria.monster;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.monster.exception.MonsterApplicationException;
import com.arcathoria.monster.exception.MonsterDomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice(basePackageClasses = MonsterController.class)
@Order(1)
class MonsterExceptionHandler {

    private static final Logger logger = LogManager.getLogger(MonsterExceptionHandler.class);
    private final ProblemDetailsFactory problemDetailsFactory;

    MonsterExceptionHandler(final ProblemDetailsFactory problemDetailsFactory) {
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({MonsterDomainException.class, MonsterApplicationException.class})
    ApiProblemDetail handleMonsterExceptions(final MonsterDomainException ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, logger);
    }
}
