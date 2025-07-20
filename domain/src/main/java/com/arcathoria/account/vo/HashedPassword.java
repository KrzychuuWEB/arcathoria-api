package com.arcathoria.account.vo;

import com.arcathoria.account.PasswordEncoder;

public class HashedPassword {

    private final String value;

    public HashedPassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }

        this.value = password;
    }

    public static HashedPassword fromRawPassword(String rawPassword, PasswordEncoder encoder) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Raw password cannot be null or blank.");
        }
        return new HashedPassword(encoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, value);
    }

    public String getValue() {
        return value;
    }
}
