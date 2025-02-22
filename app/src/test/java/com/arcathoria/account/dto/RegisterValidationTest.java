package com.arcathoria.account.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.arcathoria.ValidationTestHelper.validateAndGetFieldsWithErrors;
import static org.assertj.core.api.Assertions.assertThat;

class RegisterValidationTest {

    @Test
    void should_pass_validation_with_valid_data() {
        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "secret_password");

        Set<String> result = validateAndGetFieldsWithErrors(registerDTO);

        assertThat(result).isEmpty();
    }

    @Test
    void should_fail_validation_when_password_is_too_short() {
        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "123");

        Set<String> result = validateAndGetFieldsWithErrors(registerDTO);

        assertThat(result).containsExactly("password");
    }

    @Test
    void should_fail_validation_when_email_is_null() {
        RegisterDTO dto = new RegisterDTO(null, "secret123");

        Set<String> fieldsWithErrors = validateAndGetFieldsWithErrors(dto);

        assertThat(fieldsWithErrors).containsExactly("email");
    }

    @Test
    void should_fail_validation_when_email_is_invalid() {
        RegisterDTO dto = new RegisterDTO("invalid-email.com", "secret123");

        Set<String> fieldsWithErrors = validateAndGetFieldsWithErrors(dto);

        assertThat(fieldsWithErrors).containsExactly("email");
    }
}
