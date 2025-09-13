package com.arcathoria.combat.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class CombatNotFoundException extends ApiException {

    private final UUID combatId;

    public CombatNotFoundException(final UUID combatId) {
        super("Combat not found with id: " + combatId, "ERR_COMBAT_NOT_FOUND-404");
        this.combatId = combatId;
    }

    public UUID getCombatId() {
        return combatId;
    }
}
