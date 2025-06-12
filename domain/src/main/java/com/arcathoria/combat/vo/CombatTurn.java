package com.arcathoria.combat.vo;

import com.arcathoria.combat.CombatSide;

public record CombatTurn(CombatSide current) {

    CombatTurn changeTurn() {
        return new CombatTurn(current == CombatSide.ATTACKER ? CombatSide.DEFENDER : CombatSide.ATTACKER);
    }
}
