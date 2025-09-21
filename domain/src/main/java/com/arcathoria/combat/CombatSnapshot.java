package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;

record CombatSnapshot(
        CombatId combatId,
        ParticipantSnapshot attacker,
        ParticipantSnapshot defender,
        CombatTurn combatTurn,
        CombatType combatType,
        CombatStatus combatStatus
) {
}
