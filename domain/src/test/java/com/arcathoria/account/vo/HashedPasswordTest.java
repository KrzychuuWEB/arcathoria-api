package com.arcathoria.account.vo;

import com.arcathoria.account.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HashedPasswordTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoderTestImpl();

    @Test
    void should_hash_password_when_created() {
        String rawPassword = "password";

        HashedPassword hashedPassword = HashedPassword.fromRawPassword(rawPassword, passwordEncoder);

        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword.matches(rawPassword, passwordEncoder)).isTrue();
        assertThat(hashedPassword.getPassword()).isNotEqualTo(rawPassword);
    }

    @Test
    void should_throw_exception_when_password_is_null() {
        assertThatThrownBy(() -> {
            HashedPassword.fromRawPassword(null, passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_password_is_blank() {
        assertThatThrownBy(() -> {
            HashedPassword.fromRawPassword("", passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_match_correct_password() {
        HashedPassword hashedPassword = HashedPassword.fromRawPassword("secret", passwordEncoder);

        assertThat(hashedPassword.matches("secret", passwordEncoder)).isTrue();
    }

    @Test
    void should_not_match_incorrect_password() {
        HashedPassword hashedPassword = HashedPassword.fromRawPassword("secret", passwordEncoder);

        assertThat(hashedPassword.matches("failed", passwordEncoder)).isFalse();
    }

    @Test
    void should_return_hashed_password() {
        HashedPassword hashedPassword = HashedPassword.fromRawPassword("secret", passwordEncoder);

        assertThat(hashedPassword.getPassword()).isEqualTo("hashed-secret");
    }
}