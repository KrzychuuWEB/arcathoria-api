package com.arcathoria.combat;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CombatActionConfiguration {

    @Bean("meleeMagicDamageStrategy")
    DamageCalculator meleeMagicDamageStrategy() {
        return new MeleeMagicDamageStrategy();
    }

    @Bean
    MeleeCombatActionStrategy meleeCombatActionStrategy(
            @Qualifier("meleeMagicDamageStrategy") DamageCalculator meleeMagicDamageStrategy
    ) {
        return new MeleeCombatActionStrategy(meleeMagicDamageStrategy);
    }
}
