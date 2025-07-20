package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Participant;
import com.arcathoria.combat.vo.ParticipantMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeleeCombatActionStrategyTest {

    private CombatAction meleeCombatActionStrategy;

    @BeforeEach
    void setup() {
        meleeCombatActionStrategy = new MeleeCombatActionStrategy(new MeleeMagicDamageStrategy());
    }

    @Test
    void should_calculate_damage_for_melee_attack_and_attack_defender() {
        Participant attacker = ParticipantMother.aParticipantBuilder()
                .withIntelligence(1)
                .withHealth(150.0, 150.0)
                .build();
        Participant defender = ParticipantMother.aParticipantBuilder()
                .withHealth(100.0, 100.0)
                .build();
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat()
                .withAttacker(attacker)
                .withDefender(defender)
                .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                .build());

        meleeCombatActionStrategy.execute(combat);

        assertThat(combat.getSnapshot().defender().getHealth().getCurrent()).isEqualTo(92.0);
    }
}