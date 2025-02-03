package com.arcathoria.account.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void should_create_email_when_valid_input() {
        Email email = new Email("test@email.com");

        assertThat(email.getValue()).isEqualTo("test@email.com");
    }

    @Test
    void should_get_correct_email_with_two_domain_extension() {
        Email email = new Email("test@email.com.pl");

        assertThat(email.getValue()).isEqualTo("test@email.com.pl");
    }

    @Test
    void should_throw_exception_when_email_is_null() {
        assertThatThrownBy(() ->
                new Email(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_email_is_blank() {
        assertThatThrownBy(() ->
                new Email("")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_email_is_invalid_without_at_sign() {
        assertThatThrownBy(() ->
                new Email("testemail.com")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_email_is_invalid_without_domain() {
        assertThatThrownBy(() ->
                new Email("test@")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_email_is_invalid_without_domain_extension() {
        assertThatThrownBy(() ->
                new Email("test@email")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}