package com.arcathoria.account;

import com.arcathoria.account.vo.Email;

import java.util.Optional;

interface AccountRepository {
    boolean existsByEmail(Email email);

    Optional<Account> findByEmail(Email email);

    void save(Account account);
}
