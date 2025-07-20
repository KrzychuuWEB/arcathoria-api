package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class AccountSnapshot {
    private final AccountId accountId;
    private final Email email;
    private final HashedPassword password;

    AccountSnapshot(final AccountId accountId, final Email email, final HashedPassword password) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
    }

    AccountId getAccountId() {
        return accountId;
    }

    Email getEmail() {
        return email;
    }

    HashedPassword getPassword() {
        return password;
    }
}
