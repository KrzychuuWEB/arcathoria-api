package com.arcathoria.auth;

import com.arcathoria.testContainers.WithPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@WithPostgres
class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private JwtConfigurationProperties properties;

    private final String username = "test@example.com";
    private final UUID userId = UUID.randomUUID();

    @Test
    void should_generate_valid_token() {
        String token = jwtTokenService.generateToken(username, userId);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void should_invalidate_expired_token() {
        long previous = properties.getValidity();
        try {
            properties.setValidity(-1L);

            assertThatThrownBy(() -> jwtTokenService.generateToken(username, userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("expiresAt must be after issuedAt");
        } finally {
            properties.setValidity(previous);
        }
    }
}