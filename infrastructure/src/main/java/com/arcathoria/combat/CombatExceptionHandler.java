package com.arcathoria.combat;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.combat.exception.CombatAlreadyFinishedException;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import com.arcathoria.combat.exception.WrongTurnException;
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
class CombatExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CombatExceptionHandler.class);
    private final MessageSource messageSource;

    CombatExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CombatParticipantUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleCombatParticipantUnavailableException(final CombatParticipantUnavailableException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Could not retrieve {} participant for combat.", ex.getCombatSide());

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("combat.initial.participant.not.found", new Object[]{ex.getCombatSide()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(WrongTurnException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleWrongTurnException(final WrongTurnException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Currently the turn belongs to {}", ex.getCombatSide());

        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage("combat.combat.turn.conflict", new Object[]{ex.getCombatSide()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CombatAlreadyFinishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleCombatAlreadyFinishedException(final CombatAlreadyFinishedException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("CombatStatus for Combat {} is FINISHED", ex.getCombatId());

        return new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                messageSource.getMessage("combat.already.finished.conflict", new Object[]{ex.getCombatId()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
