package com.arcathoria;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder(final TestJwtTokenGenerator tokenGenerator) {
        return NimbusJwtDecoder.withSecretKey(tokenGenerator.getSecretKey()).build();
    }
}