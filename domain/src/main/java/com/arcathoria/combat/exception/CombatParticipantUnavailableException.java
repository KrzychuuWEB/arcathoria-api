package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.ParticipantId;

import java.util.Map;

public class CombatParticipantUnavailableException extends CombatException {

    private final ParticipantId participantId;

    public CombatParticipantUnavailableException(final ParticipantId participantId) {
        super("Could not retrieve participant for combat for id: " + participantId.value(),
                CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_UNAVAILABLE,
                Map.of("participantId", participantId.value()));
        this.participantId = participantId;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
