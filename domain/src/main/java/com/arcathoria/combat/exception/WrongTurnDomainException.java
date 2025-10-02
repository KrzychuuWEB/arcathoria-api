package com.arcathoria.combat.exception;

import com.arcathoria.combat.CombatSide;

import java.util.Map;

public class WrongTurnDomainException extends CombatDomainException {

    private final CombatSide combatSide;

    public WrongTurnDomainException(final CombatSide combatSide) {
        super("Turn belongs to " + combatSide + " you cannot perform the action now",
                CombatExceptionErrorCode.ERR_COMBAT_WRONG_TURN,
                Map.of("combatSide", combatSide.name())
        );
        this.combatSide = combatSide;
    }

    public CombatSide getCombatSide() {
        return combatSide;
    }
}
