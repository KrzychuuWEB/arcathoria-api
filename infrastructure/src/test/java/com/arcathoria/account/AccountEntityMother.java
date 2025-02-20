package com.arcathoria.account;

import java.util.UUID;

class AccountEntityMother {
    static final UUID DEFAULT_ACCOUNT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    static final String DEFAULT_EMAIL = "default@email.com";
    static final String DEFAULT_HASHED_PASSWORD = "secret";

    private UUID accountId = DEFAULT_ACCOUNT_ID;
    private String email = DEFAULT_EMAIL;
    private String hashedPassword = DEFAULT_HASHED_PASSWORD;

    private AccountEntityMother() {
    }

    static AccountEntityMother create() {
        return new AccountEntityMother();
    }

    AccountEntityMother withAccountId(UUID id) {
        this.accountId = id;
        return this;
    }

    AccountEntityMother withEmail(String email) {
        this.email = email;
        return this;
    }

    AccountEntityMother withHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    AccountEntity build() {
        return new AccountEntity(accountId, email, hashedPassword);
    }
}
