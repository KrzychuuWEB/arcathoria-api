package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;

public final class RegisterDTOMother {
    private String email;
    private String password = "default_secret_password";

    private RegisterDTOMother() {
    }

    public static RegisterDTOMother aRegisterDTO() {
        return new RegisterDTOMother();
    }

    public RegisterDTOMother withEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterDTOMother withPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterDTO build() {
        return new RegisterDTO(email, password);
    }
}
