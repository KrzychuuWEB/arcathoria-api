package com.arcathoria.account;

import com.arcathoria.account.vo.AccountEmail;

import java.util.Optional;

interface AccountRepository {
    Optional<Account> findByEmail(AccountEmail accountEmail);

    void save(Account account);
}
