package com.arcathoria.combat.vo;

import com.arcathoria.combat.CombatSide;

public class CombatTurn {

    private CombatSide current;

    public CombatTurn(final CombatSide current) {
        this.current = current;
    }

    public void changeTurn() {
        this.current = current == CombatSide.ATTACKER ? CombatSide.DEFENDER : CombatSide.ATTACKER;
    }

    public CombatSide getCurrent() {
        return current;
    }
}
