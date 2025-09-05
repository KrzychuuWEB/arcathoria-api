package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CombatTest {

    @Test
    void should_restore_from_snapshot_and_get_values_from_snapshot() {
        CombatId combatId = new CombatId(UUID.randomUUID());

        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).build());

        assertThat(combat.getSnapshot().combatId()).isEqualTo(combatId);
    }

    @Test
    void should_apply_damage_to_attacker() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        assertThat(combat.getSnapshot().attacker().getHealth().getCurrent()).isEqualTo(100.0);

        combat.applyDamageOpponent(50.0);

        assertThat(combat.getSnapshot().attacker().getHealth().getCurrent()).isEqualTo(50.0);
    }

    @Test
    void should_apply_damage_to_defender() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        assertThat(combat.getSnapshot().defender().getHealth().getCurrent()).isEqualTo(100.0);

        combat.applyDamageOpponent(50.0);

        assertThat(combat.getSnapshot().defender().getHealth().getCurrent()).isEqualTo(50.0);
    }

    @Test
    void should_false_if_defender_is_not_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        combat.applyDamageOpponent(100.0);

        assertThat(combat.isDefenderAlive()).isFalse();
    }

    @Test
    void should_true_if_defender_is__alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        assertThat(combat.isDefenderAlive()).isTrue();
    }

    @Test
    void should_false_if_attacker_is_not_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        combat.applyDamageOpponent(100.0);

        assertThat(combat.isAttackerAlive()).isFalse();
    }

    @Test
    void should_true_if_attacker_is__alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build())
                        .build()
        );

        assertThat(combat.isAttackerAlive()).isTrue();
    }

    @Test
    void change_turn_from_attacker_to_defender_and_get_current_turn() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .build()
        );

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.ATTACKER);

        combat.changeTurn();

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
    }

    @Test
    void should_return_attacker_for_attacker_turn() {
        ParticipantId uuid = new ParticipantId(UUID.randomUUID());
        Participant participant = ParticipantMother.aParticipantBuilder().withId(uuid).build();
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withAttacker(participant)
                        .withDefender(ParticipantMother.aParticipantBuilder().build())
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .build()
        );

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.ATTACKER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(participant);
        assertThat(combat.getCurrentTurnParticipant().getId()).isEqualTo(uuid);
    }

    @Test
    void should_return_defender_for_defender_turn() {
        ParticipantId uuid = new ParticipantId(UUID.randomUUID());
        Participant participant = ParticipantMother.aParticipantBuilder().withId(uuid).build();
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withDefender(participant)
                        .withAttacker(ParticipantMother.aParticipantBuilder().build())
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .build()
        );

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(participant);
        assertThat(combat.getCurrentTurnParticipant().getId()).isEqualTo(uuid);
    }
}