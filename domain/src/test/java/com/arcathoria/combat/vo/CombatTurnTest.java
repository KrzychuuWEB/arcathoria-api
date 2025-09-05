package com.arcathoria.combat.vo;

import com.arcathoria.combat.CombatSide;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CombatTurnTest {

    @Test
    void should_return_current_side() {
        CombatSide combatSide = CombatSide.ATTACKER;

        CombatTurn combatTurn = new CombatTurn(combatSide);

        assertThat(combatTurn.currentSide()).isEqualTo(combatSide);
    }

    @Test
    void should_throw_exception_when_side_is_null() {
        assertThatThrownBy(() -> new CombatTurn(null))
                .isInstanceOf(NullPointerException.class)
                .message().isEqualTo("CombatTurn side cannot be null");
    }

    @Test
    void should_change_side_when_change_turn_is_called() {
        CombatSide combatSide = CombatSide.ATTACKER;
        CombatTurn combatTurn = new CombatTurn(combatSide);

        combatTurn = combatTurn.changeTurn();

        assertThat(combatTurn.currentSide()).isEqualTo(CombatSide.DEFENDER);
    }
}