package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
                domain.getSnapshot().getAccountId().value(),
                domain.getSnapshot().getEmail().value(),
                domain.getSnapshot().getPassword().getValue()
        );
    }
}


