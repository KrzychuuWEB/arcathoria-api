package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.Participant;

record CombatSnapshot(
        CombatId combatId,
        Participant attacker,
        Participant defender
) {
}
