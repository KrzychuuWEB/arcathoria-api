package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.CombatId;

import java.util.Map;

public class CombatAlreadyFinishedException extends CombatDomainException {

    private final CombatId combatId;

    public CombatAlreadyFinishedException(final CombatId combatId) {
        super("The fight for id " + combatId + " is already finished, this action cannot be performed",
                CombatExceptionErrorCode.ERR_COMBAT_ALREADY_FINISHED,
                Map.of("combatId", combatId.value())
        );
        this.combatId = combatId;
    }

    public CombatId getCombatId() {
        return combatId;
    }
}
