package com.arcathoria.account.vo;

import com.arcathoria.account.PasswordEncoder;

class PasswordEncoderTestImpl implements PasswordEncoder {
    @Override
    public String encode(final String password) {
        return "hashed-" + password;
    }

    @Override
    public boolean matches(final String password, final String hashedPassword) {
        return hashedPassword.equals(encode(password));
    }
}