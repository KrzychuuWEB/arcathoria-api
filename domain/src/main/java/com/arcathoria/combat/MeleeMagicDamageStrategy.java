package com.arcathoria.combat;

import com.arcathoria.combat.vo.Damage;

class MeleeMagicDamageStrategy implements DamageCalculator {

    @Override
    public Damage calculate(final Participant participant) {
        return new Damage((8 + (participant.getIntelligenceLevel() - 1) * 2));
    }
}
