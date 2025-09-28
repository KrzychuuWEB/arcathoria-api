package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

import java.util.UUID;

public class AccountSnapshotMother {
    static final UUID DEFAULT_ACCOUNT_ID = UUID.randomUUID();
    static final String DEFAULT_EMAIL = "default@email.com";
    static final String DEFAULT_HASHED_PASSWORD = "secret";

    private AccountId accountId = new AccountId(DEFAULT_ACCOUNT_ID);
    private Email email = new Email(DEFAULT_EMAIL);
    private HashedPassword hashedPassword = new HashedPassword(DEFAULT_HASHED_PASSWORD);

    static AccountSnapshotMother create() {
        return new AccountSnapshotMother();
    }

    AccountSnapshotMother withAccountId(UUID id) {
        this.accountId = new AccountId(id);
        return this;
    }

    AccountSnapshotMother withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    AccountSnapshotMother withHashedPassword(String rawPassword) {
        this.hashedPassword = new HashedPassword(rawPassword);
        return this;
    }

    AccountSnapshot build() {
        return new AccountSnapshot(accountId, email, hashedPassword);
    }
}
