package com.arcathoria.combat;

import com.arcathoria.combat.vo.Damage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantTest {

    @Test
    void should_return_correct_participant() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        assertThat(participant.getId()).isEqualTo(ParticipantSnapshotMother.DEFAULT_ID);
        assertThat(participant.getHealth()).isEqualTo(ParticipantSnapshotMother.DEFAULT_HEALTH);
    }

    @Test
    void should_reduce_attacker_hp_when_defender_deals_damage() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(100, 100)
                .build());

        participant.applyDamage(new Damage(50));

        assertThat(participant.getHealth().getCurrent()).isEqualTo(50);
    }

    @Test
    void should_return_false_if_participant_is_not_alive() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(100, 100)
                .build());

        participant.applyDamage(new Damage(100));

        assertThat(participant.isAlive()).isFalse();
    }

    @Test
    void should_return_true_if_participant_is_alive() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(100, 100)
                .build());

        assertThat(participant.isAlive()).isTrue();
    }
}