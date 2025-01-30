package com.arcathoria.account.vo;

import com.arcathoria.account.PasswordEncoder;

public record HashedPassword(String password) {

    public HashedPassword {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
    }

    HashedPassword fromRawPassword(String rawPassword, PasswordEncoder encoder) {
        return new HashedPassword(encoder.encode(rawPassword));
    }

    boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, password);
    }
}
