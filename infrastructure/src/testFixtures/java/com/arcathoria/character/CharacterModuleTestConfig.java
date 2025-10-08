package com.arcathoria.character;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class CharacterModuleTestConfig {

    @Bean
    CreateCharacterE2EHelper createCharacterE2EHelper(final TestRestTemplate testRestTemplate) {
        return new CreateCharacterE2EHelper(testRestTemplate);
    }
}
