package com.arcathoria.combat;

class MeleeCombatActionStrategy implements CombatAction {

    private final DamageCalculator damageCalculator;

    MeleeCombatActionStrategy(final DamageCalculator damageCalculator) {
        this.damageCalculator = damageCalculator;
    }

    @Override
    public void execute(final Combat combat) {
        combat.applyDamageOpponent(
                damageCalculator.calculate(combat.getCurrentTurnParticipant())
        );
    }
}