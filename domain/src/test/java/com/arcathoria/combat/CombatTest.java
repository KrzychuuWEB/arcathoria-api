package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatAlreadyFinishedException;
import com.arcathoria.combat.exception.ParticipantNotFoundInCombatException;
import com.arcathoria.combat.exception.WrongTurnException;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Damage;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );

        assertThat(combat.getSnapshot().attacker().health().getCurrent()).isEqualTo(100);

        combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(50));

        assertThat(combat.getSnapshot().combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(combat.getSnapshot().attacker().health().getCurrent()).isEqualTo(50);
    }

    @Test
    void should_apply_damage_to_defender() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );

        assertThat(combat.getSnapshot().defender().health().getCurrent()).isEqualTo(100);

        combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(50));

        assertThat(combat.getSnapshot().defender().health().getCurrent()).isEqualTo(50);
    }

    @Test
    void should_false_if_defender_is_not_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );

        combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(100));

        assertThat(combat.isDefenderAlive()).isFalse();
    }

    @Test
    void should_true_if_defender_is_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .withDefender(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );

        assertThat(combat.isDefenderAlive()).isTrue();
    }

    @Test
    void should_false_if_attacker_is_not_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );

        combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(100));

        assertThat(combat.isAttackerAlive()).isFalse();
    }

    @Test
    void should_true_if_attacker_is_alive() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
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
        ParticipantSnapshot participant = ParticipantSnapshotMother.aParticipantBuilder().withId(uuid).build();
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withAttacker(participant)
                        .withDefender(ParticipantSnapshotMother.aParticipantBuilder().build())
                        .withCombatTurn(new CombatTurn(CombatSide.ATTACKER))
                        .build()
        );

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.ATTACKER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(Participant.restore(participant));
        assertThat(combat.getCurrentTurnParticipant().getId()).isEqualTo(uuid);
    }

    @Test
    void should_return_defender_for_defender_turn() {
        ParticipantId uuid = new ParticipantId(UUID.randomUUID());
        ParticipantSnapshot participant = ParticipantSnapshotMother.aParticipantBuilder().withId(uuid).build();
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withDefender(participant)
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().build())
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .build()
        );

        assertThat(combat.getCurrentTurn()).isEqualTo(CombatSide.DEFENDER);
        assertThat(combat.getCurrentTurnParticipant()).isEqualTo(Participant.restore(participant));
        assertThat(combat.getCurrentTurnParticipant().getId()).isEqualTo(uuid);
    }

    @Test
    void should_finish_combat_when_participant_health_is_zero() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().withHealth(100, 100).build())
                        .build()
        );
        CombatStatus beforeAttackCombatStatus = combat.getSnapshot().combatStatus();

        combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(combat.getSnapshot().attacker().health().getCurrent()));

        assertThat(combat.getSnapshot().attacker().health().getCurrent()).isZero();
        assertThat(combat.getSnapshot().combatStatus()).isEqualTo(CombatStatus.FINISHED);
        assertThat(beforeAttackCombatStatus).isEqualTo(CombatStatus.IN_PROGRESS);
    }

    @Test
    void should_get_combat_status() {
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().build());

        assertThat(combat.getSnapshot().combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
    }

    @Test
    void should_throw_exception_when_try_to_apply_damage_to_finished_combat() {
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(ParticipantSnapshotMother.aParticipantBuilder().build())
                        .withCombatStatus(CombatStatus.FINISHED)
                        .build()
        );

        assertThatThrownBy(() -> combat.performAttack(combat.getCurrentTurnParticipant().getId(), new Damage(combat.getSnapshot().attacker().health().getCurrent())))
                .isInstanceOf(CombatAlreadyFinishedException.class)
                .hasMessageContaining("is already finished, this action cannot be performed");
    }

    @Test
    void should_return_WrongTurnException_when_turn_does_not_belong_to_attacker() {
        ParticipantSnapshot attacker = ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build();
        ParticipantSnapshot defender = ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build();
        Combat combat = Combat.restore(
                CombatSnapshotMother.aCombat()
                        .withCombatTurn(new CombatTurn(CombatSide.DEFENDER))
                        .withAttacker(attacker)
                        .withDefender(defender)
                        .build()
        );

        assertThatThrownBy(() -> combat.performAttack(attacker.participantId(), new Damage(50)))
                .isInstanceOf(WrongTurnException.class)
                .hasMessageContaining("Turn belongs to");
    }

    @Test
    void should_return_attacker_when_attacker_is_in_combat() {
        ParticipantId participantId = new ParticipantId(UUID.randomUUID());
        ParticipantSnapshot attacker = ParticipantSnapshotMother.aParticipantBuilder().withId(participantId).build();
        Participant participant = Participant.restore(attacker);
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(attacker).build());

        Participant result = combat.getParticipant(participantId);

        assertThat(result).isEqualTo(participant);
        assertThat(result.getId()).isEqualTo(participant.getId());
    }

    @Test
    void should_return_defender_when_defender_is_in_combat() {
        ParticipantId participantId = new ParticipantId(UUID.randomUUID());
        ParticipantSnapshot defender = ParticipantSnapshotMother.aParticipantBuilder().withId(participantId).build();
        Participant participant = Participant.restore(defender);
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withDefender(defender).build());

        Participant result = combat.getParticipant(participantId);

        assertThat(result).isEqualTo(participant);
        assertThat(result.getId()).isEqualTo(participant.getId());
    }

    @Test
    void should_throw_ParticipantNotFoundInCombatException_when_participant_id_is_not_in_combat() {
        ParticipantId participantId = new ParticipantId(UUID.randomUUID());
        ParticipantSnapshot attacker = ParticipantSnapshotMother.aParticipantBuilder().build();
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(attacker).build());

        assertThatThrownBy(() -> combat.getParticipant(participantId))
                .isInstanceOf(ParticipantNotFoundInCombatException.class)
                .hasMessageContaining("Participant with id");
    }
}