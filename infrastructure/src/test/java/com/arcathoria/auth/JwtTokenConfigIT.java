package com.arcathoria.auth;

import com.arcathoria.WithPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@WithPostgres
class JwtTokenConfigIT {

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
    void should_extract_username_from_token() {
        String token = jwtTokenService.generateToken(username, userId);
        String extractedUsername = jwtTokenService.extractUserName(token);

        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void should_validate_token_successfully() {
        String token = jwtTokenService.generateToken(username, userId);

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .roles("USER")
                .build();

        assertThat(jwtTokenService.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void should_invalidate_token_for_incorrect_username() {
        properties.setValidity(1000L);
        String token = jwtTokenService.generateToken(username, userId);

        UserDetails otherUser = User.builder()
                .username("other@example.com")
                .password("password")
                .roles("USER")
                .build();

        assertThat(jwtTokenService.validateToken(token, otherUser)).isFalse();
    }

    @Test
    void should_invalidate_expired_token() {
        properties.setValidity(-1L);

        String token = jwtTokenService.generateToken(username, userId);

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .roles("USER")
                .build();

        assertThatThrownBy(() -> jwtTokenService.validateToken(token, userDetails))
                .isInstanceOf(JwtException.class);
    }
}