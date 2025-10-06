package com.arcathoria.combat;

class CombatEngine {

    private final CombatFactory combatFactory;
    private final CombatSideStrategyFactory combatSideStrategyFactory;
    private final OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy;

    CombatEngine(final CombatFactory combatFactory, final CombatSideStrategyFactory combatSideStrategyFactory,
                 final OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy) {
        this.combatFactory = combatFactory;
        this.combatSideStrategyFactory = combatSideStrategyFactory;
        this.onlyOneActiveCombatPolicy = onlyOneActiveCombatPolicy;
    }

    Combat initialCombat(final Participant attacker, final Participant defender, final CombatType combatType) {
        ensureAllowedToStart(attacker);
        ensureAllowedToStart(defender);

        CombatSide combatSide = combatSideStrategyFactory.getStrategy(combatType).choose(attacker, defender);

        return combatFactory.createCombat(attacker, defender, combatSide, combatType);
    }

    Combat handleAction(final Combat combat, final CombatAction combatAction, final Participant participant) {
        combatAction.execute(combat, participant);

        if (combat.getCombatStatus() != CombatStatus.FINISHED) {
            combat.changeTurn();
        }

        return combat;
    }

    private void ensureAllowedToStart(Participant participant) {
        if (participant.getSnapshot().participantType() != ParticipantType.PLAYER) return;
        onlyOneActiveCombatPolicy.ensureNoneActiveFor(participant.getId());
    }
}
