package com.arcathoria.combat.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class ParticipantNotHasActiveCombatsException extends ApiException {

    private final UUID participantId;

    public ParticipantNotHasActiveCombatsException(final UUID participantId) {
        super("Participant " + participantId + " not has active combats.", "ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT-404");
        this.participantId = participantId;
    }

    public UUID getParticipantId() {
        return participantId;
    }
}
