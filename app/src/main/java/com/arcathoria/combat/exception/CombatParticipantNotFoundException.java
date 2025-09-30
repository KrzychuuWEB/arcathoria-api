package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.ParticipantId;

import java.util.Map;

public class CombatParticipantNotFoundException extends CombatException {

    private final ParticipantId participantId;

    public CombatParticipantNotFoundException(final ParticipantId participantId) {
        super("Participant with id " + participantId.value() + " not found",
                CombatExceptionErrorCode.ERR_PARTICIPANT_NOT_FOUND,
                Map.of("participantId", participantId.value())
        );
        this.participantId = participantId;
    }

    public ParticipantId ParticipantId() {
        return participantId;
    }
}
