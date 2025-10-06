package com.arcathoria.account;

import com.arcathoria.account.command.CreateAccountCommand;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

final class CreateAccountCommandMother {
    static final Email DEFAULT_EMAIL = new Email("default@email.com");
    static final HashedPassword DEFAULT_HASHED_PASSWORD = new HashedPassword("secret_password");

    private Email email = DEFAULT_EMAIL;
    private HashedPassword password = DEFAULT_HASHED_PASSWORD;

    private CreateAccountCommandMother() {
    }

    static CreateAccountCommandMother aCreateAccountCommand() {
        return new CreateAccountCommandMother();
    }

    CreateAccountCommandMother withEmail(Email email) {
        this.email = email;
        return this;
    }

    CreateAccountCommandMother withPassword(HashedPassword password) {
        this.password = password;
        return this;
    }

    CreateAccountCommand build() {
        return new CreateAccountCommand(email, password);
    }
}
