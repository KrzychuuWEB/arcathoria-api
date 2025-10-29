package com.arcathoria.account;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class AccountModuleTestConfig {

    @Bean
    CreateAccountE2EHelper createAccountE2EHelper(final TestRestTemplate restTemplate) {
        return new CreateAccountE2EHelper(restTemplate);
    }
}
