package com.arcathoria.combat.vo;

import com.arcathoria.combat.CombatSide;

import java.util.Objects;

public record CombatTurn(CombatSide currentSide) {

    public CombatTurn {
        Objects.requireNonNull(currentSide, "CombatTurn side cannot be null");
    }

    public CombatTurn changeTurn() {
        return new CombatTurn(currentSide == CombatSide.ATTACKER ? CombatSide.DEFENDER : CombatSide.ATTACKER);
    }
}