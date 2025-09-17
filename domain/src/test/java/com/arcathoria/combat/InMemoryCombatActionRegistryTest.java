package com.arcathoria.combat;

import com.arcathoria.combat.exception.UnsupportedActionTypeException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryCombatActionRegistryTest {

    @Test
    void should_init_combat_registry_with_melee_attack_and_get_action_type_melee() {
        CombatActionRegistry combatActionRegistry = new InMemoryCombatActionRegistry(Map.of(
                ActionType.MELEE, new MeleeCombatActionStrategy(new MeleeMagicDamageStrategy())
        ));

        CombatAction result = combatActionRegistry.get(ActionType.MELEE);

        assertThat(result).isInstanceOf(MeleeCombatActionStrategy.class);
    }

    @Test
    void should_return_UnsupportedActionTypeException_when_action_type_not_found() {
        CombatActionRegistry combatActionRegistry = new InMemoryCombatActionRegistry(Map.of());

        assertThatThrownBy(() -> combatActionRegistry.get(ActionType.MELEE))
                .isInstanceOf(UnsupportedActionTypeException.class)
                .hasMessageContaining("This type of action is not supported");
    }
}