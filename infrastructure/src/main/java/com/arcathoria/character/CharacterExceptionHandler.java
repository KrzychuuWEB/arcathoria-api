package com.arcathoria.character;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.character.exception.CharacterNameExistsException;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.exception.SelectedCharacterNotFoundException;
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
    ApiErrorResponse handleCharacterNameExists(final CharacterNameExistsException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Character name {} exist", ex.getCharacterName());

        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage("character.create.character.name.exists", new Object[]{ex.getCharacterName()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleCharacterNotFound(final CharacterNotFoundException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Character not found with key {}", ex.getValue());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("character.get.not.found", new Object[]{ex.getValue()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(SelectedCharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleSelectedCharacterNotFoundException(final SelectedCharacterNotFoundException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Selected character not found for account {}", ex.getId());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("character.get.character.selected.not.found", null, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
