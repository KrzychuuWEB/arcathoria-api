package com.arcathoria.combat.exception;

import com.arcathoria.ApiException;
import com.arcathoria.combat.CombatSide;

public class CombatParticipantUnavailableException extends ApiException {

    public CombatParticipantUnavailableException(final CombatSide side) {
        super(buildMessage(side), "ERR_COMBAT_PARTICIPANT_UNAVAILABLE-400");
    }

    private static String buildMessage(CombatSide side) {
        return switch (side) {
            case ATTACKER -> "Could not retrieve attacker participant for combat.";
            case DEFENDER -> "Could not retrieve defender participant for combat.";
        };
    }
}
