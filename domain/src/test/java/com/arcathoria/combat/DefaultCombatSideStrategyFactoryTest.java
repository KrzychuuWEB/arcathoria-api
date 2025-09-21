package com.arcathoria.combat;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCombatSideStrategyFactoryTest {
    
    @Test
    void should_return_lowest_health_starts_strategy() {
        DefaultCombatSideStrategyFactory factory = new DefaultCombatSideStrategyFactory();

        CombatSideStrategy result = factory.getStrategy(CombatType.PVE);

        assertThat(result).isInstanceOf(LowestHealthStartsStrategy.class);
    }
}