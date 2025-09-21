package com.arcathoria.combat.exception;

import com.arcathoria.DomainException;
import com.arcathoria.combat.CombatSide;

public class WrongTurnException extends DomainException {

    private final CombatSide combatSide;

    public WrongTurnException(final CombatSide combatSide) {
        super("Turn belongs to " + combatSide + " you cannot perform the action now", "ERR_COMBAT_WRONG_TURN");
        this.combatSide = combatSide;
    }

    public CombatSide getCombatSide() {
        return combatSide;
    }
}
