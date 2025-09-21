package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CombatActionRegistryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private CombatActionRegistryAdapter registryAdapter;

    @Test
    void should_get_melee_attack_strategy_when_action_is_melee() {
        CombatAction result = registryAdapter.get(ActionType.MELEE);

        assertThat(result).isInstanceOf(MeleeCombatActionStrategy.class);
    }
}