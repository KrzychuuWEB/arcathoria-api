package com.arcathoria.combat;

import com.arcathoria.combat.exception.OnlyOneActiveCombatAllowedException;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CombatEngineTest {

    private CombatEngine combatEngine;
    private CombatAction meleeCombatActionStrategy;
    private final FakeCombatSessionPort fakeCombatSessionPort = new FakeCombatSessionPort();

    @BeforeEach
    void setup() {
        final CombatFactory combatFactory = new CombatFactory();
        final CombatSideStrategyFactory combatSideStrategyFactory = new DefaultCombatSideStrategyFactory();
        final DamageCalculator meleeMagicDamageStrategy = new MeleeMagicDamageStrategy();
        final OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy = new OnlyOneActiveCombatPolicy(fakeCombatSessionPort);

        meleeCombatActionStrategy = new MeleeCombatActionStrategy(meleeMagicDamageStrategy);
        combatEngine = new CombatEngine(combatFactory, combatSideStrategyFactory, onlyOneActiveCombatPolicy);
    }

    @Test
    void should_return_new_combat_pve_with_attacker_and_defender_with_health_combat_side_strategy() {
        ParticipantId uuid = new ParticipantId(UUID.randomUUID());
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(80, 80).withId(uuid).build());
        Combat combat = combatEngine.initialCombat(attacker, defender, CombatType.PVE);

        assertThat(combat.getSnapshot().attacker().participantId()).isEqualTo(ParticipantSnapshotMother.DEFAULT_ID);
        assertThat(combat.getSnapshot().defender().participantId()).isEqualTo(uuid);
        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(defender);
        assertThat(combat.getSnapshot().combatType()).isEqualTo(CombatType.PVE);
    }

    @Test
    void should_defender_melee_magic_attack_to_attacker_and_attacker_change_health_after_attack() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(80, 80).build());

        Combat combat = combatEngine.initialCombat(attacker, defender, CombatType.PVE);
        Combat result = combatEngine.handleAction(combat, meleeCombatActionStrategy, defender);

        assertThat(result.getSnapshot().attacker().health().getCurrent()).isEqualTo(92);
        assertThat(result.getSnapshot().defender().health().getCurrent()).isEqualTo(80);
        assertThat(result.getSnapshot().combatTurn().currentSide()).isEqualTo(CombatSide.ATTACKER);
    }

    @Test
    void should_return_in_progress_combat_status_after_execute_action() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(80, 80).build());

        Combat combat = combatEngine.initialCombat(attacker, defender, CombatType.PVE);

        combatEngine.handleAction(combat, meleeCombatActionStrategy, defender);

        assertThat(combat.getCombatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
    }

    @Test
    void should_return_finish_combat_status_after_execute_action() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(2, 100).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(80, 80).build());

        Combat combat = combatEngine.initialCombat(attacker, defender, CombatType.PVE);

        combatEngine.handleAction(combat, meleeCombatActionStrategy, defender);

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
        assertThat(combat.getCombatStatus()).isEqualTo(CombatStatus.FINISHED);
        assertThat(combat.getSnapshot().attacker().health().getCurrent()).isZero();
    }

    @Test
    void should_return_OnlyOneActiveCombatAllowedException_for_init_combat_when_attacker_has_active_combat() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).withHealth(2, 100).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withHealth(80, 80).build());

        fakeCombatSessionPort.save(CombatSnapshotMother.aCombat().withAttacker(attacker.getSnapshot()).build());

        assertThatThrownBy(() -> combatEngine.initialCombat(attacker, defender, CombatType.PVE))
                .isInstanceOf(OnlyOneActiveCombatAllowedException.class)
                .hasMessageContaining(attacker.getId().value().toString());
    }
}