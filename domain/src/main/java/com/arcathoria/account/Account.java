package com.arcathoria.account;

import com.arcathoria.account.vo.AccountEmail;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.HashedPassword;

class Account {

    private final AccountId accountId;
    private final AccountEmail email;
    private final HashedPassword password;

    private Account(final AccountId accountId, final AccountEmail email, final HashedPassword password) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
    }

    Account fromRawPassword(String rawPassword, PasswordEncoder encoder) {
        return new Account(this.accountId, this.email, encoder.encode(rawPassword));
    }

    boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, password);
    }

    AccountId getAccountId() {
        return accountId;
    }

    AccountEmail getEmail() {
        return email;
    }

    HashedPassword getPassword() {
        return password;
    }
}
