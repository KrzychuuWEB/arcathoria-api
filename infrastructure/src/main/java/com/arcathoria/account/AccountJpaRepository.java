package com.arcathoria.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.arcathoria.account.AccountMapper.mapToDomain;
import static com.arcathoria.account.AccountMapper.mapToEntity;

interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

}

@Repository
class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    AccountRepositoryAdapter(final AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Account save(final Account account) {
        AccountEntity entity = mapToEntity(account);
        AccountEntity savedAccount = accountJpaRepository.save(entity);
        return mapToDomain(savedAccount);
    }
}


