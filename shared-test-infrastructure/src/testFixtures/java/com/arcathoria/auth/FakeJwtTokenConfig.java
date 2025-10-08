package com.arcathoria.auth;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FakeJwtTokenConfig {

    @Bean
    TestJwtTokenGenerator testJwtTokenGenerator() {
        return new TestJwtTokenGenerator();
    }

    @Bean
    AccountWithAuthenticated accountWithAuthenticated(
            final TestJwtTokenGenerator tokenGenerator
    ) {
        return new AccountWithAuthenticated(tokenGenerator);
    }
}
