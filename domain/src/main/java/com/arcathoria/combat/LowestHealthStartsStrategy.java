package com.arcathoria.combat;

class LowestHealthStartsStrategy implements CombatSideStrategy {

    @Override
    public CombatSide choose(final Participant attacker, final Participant defender) {
        int attackerMaxHp = attacker.getHealth().getMax();
        int defenderMaxHp = defender.getHealth().getMax();

        if (attackerMaxHp < defenderMaxHp) {
            return CombatSide.ATTACKER;
        }
        if (defenderMaxHp < attackerMaxHp) {
            return CombatSide.DEFENDER;
        }

        return CombatSide.ATTACKER;
    }
}
