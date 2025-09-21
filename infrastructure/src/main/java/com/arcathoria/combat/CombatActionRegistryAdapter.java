package com.arcathoria.combat;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
class CombatActionRegistryAdapter implements CombatActionRegistry {

    private final CombatActionRegistry combatActionRegistry;

    CombatActionRegistryAdapter(final MeleeCombatActionStrategy meleeCombatActionStrategy) {
        Map<ActionType, CombatAction> strategies = Map.of(
                ActionType.MELEE, meleeCombatActionStrategy
        );

        this.combatActionRegistry = new InMemoryCombatActionRegistry(strategies);
    }

    @Override
    public CombatAction get(final ActionType actionType) {
        return combatActionRegistry.get(actionType);
    }
}
