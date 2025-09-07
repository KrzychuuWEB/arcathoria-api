package com.arcathoria.combat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthCombatSideStrategyTest {

    private CombatSideStrategy healthCombatSideStrategy;

    @BeforeEach
    void setup() {
        healthCombatSideStrategy = new HealthCombatSideStrategy();
    }

    @Test
    void should_return_defender_side_for_choose_side_that_has_more_health() {
        Participant attacker = ParticipantMother.aParticipantBuilder()
                .withHealth(100, 100)
                .build();
        Participant defender = ParticipantMother.aParticipantBuilder()
                .withHealth(90, 90)
                .build();

        CombatSide combatSide = healthCombatSideStrategy.choose(attacker, defender);

        assertThat(combatSide).isEqualTo(CombatSide.DEFENDER);
    }

    @Test
    void should_return_attacker_side_for_choose_side_that_has_more_health() {
        Participant attacker = ParticipantMother.aParticipantBuilder()
                .withHealth(50, 50)
                .build();
        Participant defender = ParticipantMother.aParticipantBuilder()
                .withHealth(90, 90)
                .build();

        CombatSide combatSide = healthCombatSideStrategy.choose(attacker, defender);

        assertThat(combatSide).isEqualTo(CombatSide.ATTACKER);
    }
}