package com.arcathoria.character;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.character.exception.CharacterNameExistsException;
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
class CharacterExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CharacterExceptionHandler.class);
    private final MessageSource messageSource;

    CharacterExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CharacterNameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleCharacterNameExists(CharacterNameExistsException ex, HttpServletRequest request, Locale locale) {
        logger.warn("Character name {} exist", ex.getCharacterName());

        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage(ex.getMessage(), new Object[]{ex.getCharacterName()}, locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
