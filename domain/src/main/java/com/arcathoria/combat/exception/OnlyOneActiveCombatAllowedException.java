package com.arcathoria.combat.exception;

import com.arcathoria.DomainException;
import com.arcathoria.combat.vo.ParticipantId;

public class OnlyOneActiveCombatAllowedException extends DomainException {

    private final ParticipantId participantId;

    public OnlyOneActiveCombatAllowedException(final ParticipantId participantId) {
        super("Participant " + participantId.value() + " already has an active combat", "ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT");
        this.participantId = participantId;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
