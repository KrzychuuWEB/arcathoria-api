package com.arcathoria.combat;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.combat.exception.*;
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
        logger.warn("Could not retrieve {} participant for combat.", ex.getId());

        return new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("combat.initial.participant.not.found", new Object[]{ex.getId()}, ex.getMessage(), locale),
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
                messageSource.getMessage("combat.turn.conflict", new Object[]{ex.getCombatSide()}, ex.getMessage(), locale),
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

    @ExceptionHandler(CombatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleCombatNotFoundException(final CombatNotFoundException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Combat not found with id: {}", ex.getCombatId());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("combat.not.found", new Object[]{ex.getCombatId()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UnsupportedActionTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleUnsupportedActionTypeException(final UnsupportedActionTypeException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Action type not supported: {}", ex.getActionType());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("combat.action.type.not.found", new Object[]{ex.getActionType()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ParticipantNotFoundInCombatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleParticipantNotFoundException(final ParticipantNotFoundInCombatException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Participant with id: {} not found in combat: {}", ex.getParticipantId(), ex.getCombatId());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("combat.participant.not.found", new Object[]{ex.getParticipantId(), ex.getCombatId()}, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ParticipantNotHasActiveCombatsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleSelectedCharacterNotFoundException(final ParticipantNotHasActiveCombatsException ex, final HttpServletRequest request, final Locale locale) {
        logger.warn("Participant with id {} not has active combats", ex.getParticipantId());

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                messageSource.getMessage("character.get.combat.by.participant.id", null, ex.getMessage(), locale),
                ex.getErrorCode(),
                request.getRequestURI()
        );
    }
}
