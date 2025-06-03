package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipantTest {

    @Test
    void should_return_correct_participant() {
        Participant participant = ParticipantMother.aParticipantBuilder().build();

        assertThat(participant.getId()).isEqualTo(ParticipantMother.DEFAULT_ID);
        assertThat(participant.getHealth()).isEqualTo(ParticipantMother.DEFAULT_HEALTH);
    }

    @Test
    void should_apply_damage() {
        Participant participant = ParticipantMother.aParticipantBuilder()
                .withHealth(100.0, 100.0)
                .build();

        participant.applyDamage(50);

        assertThat(participant.getHealth().getCurrent()).isEqualTo(50.0);
    }
}