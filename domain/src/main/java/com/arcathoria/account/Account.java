package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class Account {
    static Account restore(AccountSnapshot snapshot) {
        return new Account(
                snapshot.getAccountId(),
                snapshot.getEmail(),
                snapshot.getPassword()
        );
    }

    private final AccountId accountId;
    private final Email email;
    private final HashedPassword password;

    private Account(final AccountId accountId, final Email email, final HashedPassword password) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
    }

    AccountSnapshot getSnapshot() {
        return new AccountSnapshot(
                accountId,
                email,
                password
        );
    }
}
