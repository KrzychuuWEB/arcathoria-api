package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatNotFoundException;
import com.arcathoria.combat.vo.CombatId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class GetCombatSnapshotFromStore {

    private static final Logger log = LogManager.getLogger(GetCombatSnapshotFromStore.class);
    private final CombatSessionStore combatSessionStore;

    GetCombatSnapshotFromStore(final CombatSessionStore combatSessionStore) {
        this.combatSessionStore = combatSessionStore;
    }

    CombatSnapshot getSnapshotById(final CombatId combatId) {
        return combatSessionStore.getCombatById(combatId)
                .orElseThrow(() -> {
                    log.warn("Combat not found with id: {}", combatId.value());
                    return new CombatNotFoundException(combatId.value());
                });
    }
}
