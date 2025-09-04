package com.arcathoria.combat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CombatEngineTest {

    private CombatEngine combatEngine;
    private CombatAction meleeCombatActionStrategy;

    @BeforeEach
    void setup() {
        final CombatFactory combatFactory = new CombatFactory();
        final CombatSideStrategy combatSideStrategy = new HealthCombatSideStrategy();
        final DamageCalculator meleeMagicDamageStrategy = new MeleeMagicDamageStrategy();

        meleeCombatActionStrategy = new MeleeCombatActionStrategy(meleeMagicDamageStrategy);
        combatEngine = new CombatEngine(combatFactory, combatSideStrategy);
    }

    @Test
    void should_return_new_combat_pve_with_attacker_and_defender_with_health_combat_side_strategy() {
        UUID uuid = UUID.randomUUID();
        Participant attacker = ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build();
        Participant defender = ParticipantMother.aParticipantBuilder().withHealth(80.0, 80.0).withId(uuid).build();
        Combat combat = combatEngine.startCombat(attacker, defender, CombatType.PVE);

        assertThat(combat.getSnapshot().attacker().getId()).isEqualTo(ParticipantMother.DEFAULT_ID);
        assertThat(combat.getSnapshot().defender().getId()).isEqualTo(uuid);
        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(defender);
        assertThat(combat.getSnapshot().combatType()).isEqualTo(CombatType.PVE);
    }

    @Test
    void should_defender_melee_magic_attack_to_attacker_and_attacker_change_health_after_attack() {
        Participant attacker = ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build();
        Participant defender = ParticipantMother.aParticipantBuilder().withHealth(80.0, 80.0).build();

        Combat combat = combatEngine.startCombat(attacker, defender, CombatType.PVE);
        combatEngine.handleAction(combat, meleeCombatActionStrategy);

        assertThat(combat.getSnapshot().attacker().getHealth().getCurrent()).isEqualTo(92.0);
        assertThat(combat.getSnapshot().defender().getHealth().getCurrent()).isEqualTo(80.0);
        assertThat(combat.getSnapshot().combatTurn().getCurrent()).isEqualTo(CombatSide.ATTACKER);
    }
}