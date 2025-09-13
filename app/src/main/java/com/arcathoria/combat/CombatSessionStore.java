package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;

import java.util.Optional;

interface CombatSessionStore {

    CombatSnapshot save(final CombatSnapshot snapshot);

    Optional<CombatSnapshot> getCombatById(final CombatId combatId);
}
