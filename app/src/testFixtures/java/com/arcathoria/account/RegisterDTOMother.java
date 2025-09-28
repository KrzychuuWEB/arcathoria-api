package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;

final class RegisterDTOMother {
    private String email;
    private String password = "default_secret_password";

    private RegisterDTOMother() {
    }

    static RegisterDTOMother aRegisterDTO() {
        return new RegisterDTOMother();
    }

    RegisterDTOMother withEmail(String email) {
        this.email = email;
        return this;
    }

    RegisterDTOMother withPassword(String password) {
        this.password = password;
        return this;
    }

    RegisterDTO build() {
        return new RegisterDTO(email, password);
    }
}
