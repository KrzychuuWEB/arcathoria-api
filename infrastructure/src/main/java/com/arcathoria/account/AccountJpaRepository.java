package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<AccountEntity> findByEmail(String email);
}

@Repository
class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    AccountRepositoryAdapter(final AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public boolean existsByEmail(final Email email) {
        return accountJpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public Optional<Account> findByEmail(final Email email) {
        return accountJpaRepository.findByEmail(email.getValue()).map(this::mapToDomain);
    }

    @Override
    public Account save(final Account account) {
        AccountEntity entity = mapToEntity(account);
        AccountEntity savedAccount = accountJpaRepository.save(entity);
        return mapToDomain(savedAccount);
    }

    private Account mapToDomain(AccountEntity entity) {
        return Account.restore(
                new AccountSnapshot(
                        new AccountId(entity.getId()),
                        new Email(entity.getEmail()),
                        new HashedPassword(entity.getPassword()
                        )
                ));
    }

    private AccountEntity mapToEntity(Account domain) {
        return new AccountEntity(
                domain.getSnapshot().getAccountId().getValue(),
                domain.getSnapshot().getEmail().getValue(),
                domain.getSnapshot().getPassword().getValue()
        );
    }
}


