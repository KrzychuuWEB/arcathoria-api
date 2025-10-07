package com.arcathoria.combat;

class DefaultCombatSideStrategyFactory implements CombatSideStrategyFactory {

    private final LowestHealthStartsStrategy lowestHealthStartsStrategy;

    DefaultCombatSideStrategyFactory() {
        this.lowestHealthStartsStrategy = new LowestHealthStartsStrategy();
    }

    @Override
    public CombatSideStrategy getStrategy(final CombatType combatType) {
        switch (combatType) {
            case PVE:
                return selectPVEStrategy();
            default:
                return lowestHealthStartsStrategy;
        }
    }

    private CombatSideStrategy selectPVEStrategy() {
        return lowestHealthStartsStrategy;
    }
}
