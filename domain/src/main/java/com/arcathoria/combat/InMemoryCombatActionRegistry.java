package com.arcathoria.combat;

import com.arcathoria.combat.exception.UnsupportedActionTypeDomainException;

import java.util.Map;
import java.util.Optional;

class InMemoryCombatActionRegistry implements CombatActionRegistry {

    private final Map<ActionType, CombatAction> strategies;

    InMemoryCombatActionRegistry(final Map<ActionType, CombatAction> strategies) {
        this.strategies = Map.copyOf(strategies);
    }

    @Override
    public CombatAction get(final ActionType actionType) {
        return Optional.ofNullable(strategies.get(actionType))
                .orElseThrow(() -> new UnsupportedActionTypeDomainException(actionType));
    }
}
