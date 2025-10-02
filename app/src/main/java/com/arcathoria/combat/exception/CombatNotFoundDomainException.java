package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.CombatId;

import java.util.Map;

public class CombatNotFoundDomainException extends CombatDomainException {

    private final CombatId combatId;

    public CombatNotFoundDomainException(final CombatId combatId) {
        super("Combat not found with id: " + combatId,
                CombatExceptionErrorCode.ERR_COMBAT_NOT_FOUND,
                Map.of("combatId", combatId.value())
        );
        this.combatId = combatId;
    }

    public CombatId getCombatId() {
        return combatId;
    }
}
