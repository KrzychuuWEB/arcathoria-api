package com.arcathoria.account.vo;

public class HashedPassword {

    private final String password;

    public HashedPassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }

        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
