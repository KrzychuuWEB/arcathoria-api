package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

class ExecuteCombatActionUseCase {

    private final CombatEngine combatEngine;

    ExecuteCombatActionUseCase(final CombatEngine combatEngine) {
        this.combatEngine = combatEngine;
    }

    void execute(final CombatId combatId, final ParticipantId participantId) {

    }
}