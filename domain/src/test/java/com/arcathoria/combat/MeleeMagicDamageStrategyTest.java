package com.arcathoria.combat;

import com.arcathoria.character.vo.Level;
import com.arcathoria.combat.vo.Damage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MeleeMagicDamageStrategyTest {

    @Test
    void should_calculate_melee_damage_based_on_intelligence() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withIntelligence(new Level(1)).build());
        MeleeMagicDamageStrategy meleeMagicDamageStrategy = new MeleeMagicDamageStrategy();

        Damage damage = meleeMagicDamageStrategy.calculate(participant);

        assertThat(damage.value()).isEqualTo(8);
    }

    @Test
    void should_calculate_melee_damage_based_on_higher_intelligence_level() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withIntelligence(new Level(10)).build());
        MeleeMagicDamageStrategy meleeMagicDamageStrategy = new MeleeMagicDamageStrategy();

        Damage damage = meleeMagicDamageStrategy.calculate(participant);

        assertThat(damage.value()).isBetween(20, 30);
    }
}