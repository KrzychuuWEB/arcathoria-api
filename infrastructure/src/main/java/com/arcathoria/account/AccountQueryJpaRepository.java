package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

interface AccountQueryJpaRepository extends JpaRepository<AccountEntity, UUID> {

    boolean existsByEmail(String email);

    Optional<AccountEntity> findByEmail(String email);
}

@Repository
class AccountQueryRepositoryAdapter implements AccountQueryRepository {

    private final AccountQueryJpaRepository jpaRepository;

    AccountQueryRepositoryAdapter(final AccountQueryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByEmail(final Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public Optional<Account> findByEmail(final Email email) {
        return jpaRepository.findByEmail(email.value()).map(AccountMapper::mapToDomain);
    }

    @Override
    public Optional<Account> findById(final AccountId accountId) {
        return jpaRepository.findById(accountId.value()).map(AccountMapper::mapToDomain);
    }
}