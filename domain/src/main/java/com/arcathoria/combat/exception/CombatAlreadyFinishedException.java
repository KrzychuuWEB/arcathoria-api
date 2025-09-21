package com.arcathoria.combat.exception;

import com.arcathoria.DomainException;
import com.arcathoria.combat.vo.CombatId;

public class CombatAlreadyFinishedException extends DomainException {

    private final CombatId combatId;

    public CombatAlreadyFinishedException(final CombatId combatId) {
        super("The fight for id {} is already finished, this action cannot be performed", "ERR_COMBAT_ALREADY_FINISHED");
        this.combatId = combatId;
    }

    public CombatId getCombatId() {
        return combatId;
    }
}
