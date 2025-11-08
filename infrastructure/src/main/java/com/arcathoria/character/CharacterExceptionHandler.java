package com.arcathoria.character;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.ProblemDetailsFactory;
import com.arcathoria.character.exception.CharacterApplicationException;
import com.arcathoria.character.exception.CharacterDomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice(basePackageClasses = CharacterController.class)
@Order(1)
class CharacterExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CharacterExceptionHandler.class);
    private final ProblemDetailsFactory problemDetailsFactory;

    CharacterExceptionHandler(final ProblemDetailsFactory problemDetailsFactory) {
        this.problemDetailsFactory = problemDetailsFactory;
    }

    @ExceptionHandler({CharacterDomainException.class, CharacterApplicationException.class})
    ApiProblemDetail handleCharacterExceptions(final CharacterDomainException ex, final HttpServletRequest request, final Locale locale) {
        return problemDetailsFactory.build(ex, request.getRequestURI(), locale, logger);
    }
}
