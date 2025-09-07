package com.arcathoria.combat;

class MeleeMagicDamageStrategy implements DamageCalculator {

    @Override
    public int calculate(final Participant participant) {
        return (8 + (participant.getIntelligenceLevel() - 1) * 2);
    }
}
