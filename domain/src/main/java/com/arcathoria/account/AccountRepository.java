package com.arcathoria.account;

import com.arcathoria.account.vo.Email;

import java.util.Optional;

interface AccountRepository {
    Optional<Account> findByEmail(Email email);

    void save(Account account);
}
