package com.arcathoria.monster;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Order(1)
class MonsterExceptionHandler {

    private static final Logger logger = LogManager.getLogger(MonsterExceptionHandler.class);
    private final MessageSource messageSource;

    MonsterExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MonsterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleMonsterNotFoundException(final MonsterNotFoundException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Monster not found for id: " + ex.getMonsterId());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("monster.not.found", new Object[]{ex.getMonsterId()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
