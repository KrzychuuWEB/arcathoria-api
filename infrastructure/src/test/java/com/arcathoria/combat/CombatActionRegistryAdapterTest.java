package com.arcathoria.combat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@ContextConfiguration(classes = {
        CombatActionRegistryAdapter.class,
        MeleeCombatActionStrategy.class,
})
class CombatActionRegistryAdapterTest {

    @MockitoBean
    private DamageCalculator damageCalculator;

    @Autowired
    private CombatActionRegistryAdapter registryAdapter;

    @Test
    void should_get_melee_attack_strategy_when_action_is_melee() {
        CombatAction result = registryAdapter.get(ActionType.MELEE);

        assertThat(result).isInstanceOf(MeleeCombatActionStrategy.class);
    }
}