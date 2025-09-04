package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;

record CombatState(
        CombatId combatId,
        Participant attacker,
        Participant defender,
        CombatSide combatSide
) {
}
