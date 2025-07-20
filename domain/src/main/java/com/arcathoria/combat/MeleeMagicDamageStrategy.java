package com.arcathoria.combat;

import com.arcathoria.combat.vo.Participant;

class MeleeMagicDamageStrategy implements DamageCalculator {

    @Override
    public double calculate(final Participant participant) {
        return (8 + (participant.getIntelligenceLevel() - 1) * 2);
    }
}
