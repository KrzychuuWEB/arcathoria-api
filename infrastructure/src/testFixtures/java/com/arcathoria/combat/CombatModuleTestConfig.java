package com.arcathoria.combat;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class CombatModuleTestConfig {

    @Bean
    CreateCombatE2EHelper createCombatE2EHelper(final TestRestTemplate testRestTemplate) {
        return new CreateCombatE2EHelper(testRestTemplate);
    }
}
