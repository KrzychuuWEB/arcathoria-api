package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;

interface CombatSessionStore {

    CombatSnapshot save(final CombatSnapshot snapshot);

    CombatSnapshot getCombatById(final CombatId combatId);
}
