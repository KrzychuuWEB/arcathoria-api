package com.arcathoria.combat.vo;

import com.arcathoria.combat.CombatSide;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CombatTurnTest {

    @Test
    void should_return_attacker_turn() {
        CombatTurn combatTurn = new CombatTurn(CombatSide.ATTACKER);

        assertThat(combatTurn.getCurrent()).isEqualTo(CombatSide.ATTACKER);
    }

    @Test
    void should_return_defender_after_change_turn() {
        CombatTurn combatTurn = new CombatTurn(CombatSide.ATTACKER);

        combatTurn.changeTurn();

        assertThat(combatTurn.getCurrent()).isEqualTo(CombatSide.DEFENDER);
    }
}