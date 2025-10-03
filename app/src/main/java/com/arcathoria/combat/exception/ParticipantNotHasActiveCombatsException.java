package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.ParticipantId;

import java.util.Map;

public class ParticipantNotHasActiveCombatsException extends CombatDomainException {

    private final ParticipantId participantId;

    public ParticipantNotHasActiveCombatsException(final ParticipantId participantId) {
        super("Participant " + participantId + " not has active combats.",
                CombatExceptionErrorCode.ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT,
                Map.of("participantId", participantId.value())
        );
        this.participantId = participantId;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
