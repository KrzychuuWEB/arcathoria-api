package com.arcathoria.combat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LowestHealthStartsStrategyTest {

    private CombatSideStrategy healthCombatSideStrategy;

    @BeforeEach
    void setup() {
        healthCombatSideStrategy = new LowestHealthStartsStrategy();
    }

    @Test
    void should_return_defender_side_for_choose_side_that_has_low_health() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(100, 100)
                .build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(90, 90)
                .build());

        CombatSide combatSide = healthCombatSideStrategy.choose(attacker, defender);

        assertThat(combatSide).isEqualTo(CombatSide.DEFENDER);
    }

    @Test
    void should_return_attacker_side_for_choose_side_that_has_low_health() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(50, 50)
                .build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder()
                .withHealth(90, 90)
                .build());

        CombatSide combatSide = healthCombatSideStrategy.choose(attacker, defender);

        assertThat(combatSide).isEqualTo(CombatSide.ATTACKER);
    }
}