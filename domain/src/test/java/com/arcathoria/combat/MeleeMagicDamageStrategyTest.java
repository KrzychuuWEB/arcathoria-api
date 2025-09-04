package com.arcathoria.combat;

import com.arcathoria.combat.vo.ParticipantMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeleeMagicDamageStrategyTest {

    @Test
    void should_calculate_melee_damage_based_on_intelligence() {
        Participant participant = ParticipantMother.aParticipantBuilder().withIntelligence(1).build();
        MeleeMagicDamageStrategy meleeMagicDamageStrategy = new MeleeMagicDamageStrategy();

        double damage = meleeMagicDamageStrategy.calculate(participant);

        assertThat(damage).isEqualTo(8);
    }
}