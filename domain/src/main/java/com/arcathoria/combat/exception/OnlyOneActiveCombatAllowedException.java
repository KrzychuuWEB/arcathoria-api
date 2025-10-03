package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.ParticipantId;

import java.util.Map;

public class OnlyOneActiveCombatAllowedException extends CombatDomainException {

    private final ParticipantId participantId;

    public OnlyOneActiveCombatAllowedException(final ParticipantId participantId) {
        super("Participant " + participantId.value() + " already has an active combat",
                CombatExceptionErrorCode.ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT,
                Map.of("participantId", participantId.value())
        );
        this.participantId = participantId;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
