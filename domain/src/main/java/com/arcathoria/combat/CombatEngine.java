package com.arcathoria.combat;

class CombatEngine {

    private final CombatFactory combatFactory;
    private final CombatSideStrategy combatSideStrategy;

    CombatEngine(final CombatFactory combatFactory, final CombatSideStrategy combatSideStrategy) {
        this.combatFactory = combatFactory;
        this.combatSideStrategy = combatSideStrategy;
    }

    Combat initialCombat(final Participant attacker, final Participant defender, final CombatType combatType) {
        CombatSide combatSide = combatSideStrategy.choose(attacker, defender);
        return combatFactory.createCombat(attacker, defender, combatSide, combatType);
    }

    void handleAction(final Combat combat, final CombatAction combatAction) {
        combatAction.execute(combat);
        
        if (combat.getCombatStatus() != CombatStatus.FINISHED) {
            combat.changeTurn();
        }
    }
}
