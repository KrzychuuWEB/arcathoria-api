package com.arcathoria.combat;

class HigherHealthStartsStrategy implements CombatSideStrategy {

    @Override
    public CombatSide choose(final Participant attacker, final Participant defender) {
        int attackerMax = attacker.getHealth().getMax();
        int defenderMax = defender.getHealth().getMax();

        if (attackerMax > defenderMax) {
            return CombatSide.DEFENDER;
        }
        if (attackerMax < defenderMax) {
            return CombatSide.ATTACKER;
        }

        return CombatSide.ATTACKER;
    }
}
