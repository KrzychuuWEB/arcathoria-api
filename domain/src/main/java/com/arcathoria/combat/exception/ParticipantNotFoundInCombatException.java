package com.arcathoria.combat.exception;

import com.arcathoria.DomainException;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

public class ParticipantNotFoundInCombatException extends DomainException {

    private final CombatId combatId;
    private final ParticipantId participantId;

    public ParticipantNotFoundInCombatException(final CombatId combatId, final ParticipantId participantId) {
        super("Participant with id" + participantId + "not found in combat " + combatId, "ERR_COMBAT_PARTICIPANT_NOT_FOUND_IN_COMBAT");
        this.combatId = combatId;
        this.participantId = participantId;
    }

    public CombatId getCombatId() {
        return combatId;
    }

    public ParticipantId getParticipantId() {
        return participantId;
    }
}
