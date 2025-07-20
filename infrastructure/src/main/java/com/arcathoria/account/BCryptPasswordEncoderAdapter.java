package com.arcathoria.account;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    BCryptPasswordEncoderAdapter(final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String encode(final String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(final String password, final String hashedPassword) {
        return bCryptPasswordEncoder.matches(password, hashedPassword);
    }
}
