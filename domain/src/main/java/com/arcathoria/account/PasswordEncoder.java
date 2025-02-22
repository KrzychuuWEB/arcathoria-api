package com.arcathoria.account;

public interface PasswordEncoder {

    String encode(String password);

    boolean matches(String password, String hashedPassword);
}
