package com.arcathoria.account;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class AccountQueryRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private AccountQueryRepositoryAdapter accountQueryRepositoryAdapter;

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Test
    void should_find_account_by_email() {
        Account account = persistAccount(AccountSnapshotMother.create().build());

        Optional<Account> result = accountQueryRepositoryAdapter.findByEmail(account.getSnapshot().getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getSnapshot().getAccountId().value()).isNotNull();
        assertThat(result.get().getSnapshot().getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.get().getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }

    @Test
    void should_return_empty_optional_when_email_not_found() {
        Optional<Account> result = accountQueryRepositoryAdapter.findByEmail(new Email("invalid@email.com"));

        assertThat(result).isEmpty();
    }

    @Test
    void should_return_true_if_email_exists() {
        Email usedEmail = new Email("used@email.com");
        persistAccount(AccountSnapshotMother.create().withEmail(usedEmail.value()).build());

        boolean result = accountQueryRepositoryAdapter.existsByEmail(usedEmail);

        assertThat(result).isTrue();
    }

    @Test
    void should_return_account_by_id() {
        Email accountEmail = new Email("example@email.com");
        Account account = persistAccount(AccountSnapshotMother.create().withEmail(accountEmail.value()).build());

        AccountId accountId = account.getSnapshot().getAccountId();

        Optional<Account> result = accountQueryRepositoryAdapter.findById(accountId);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getSnapshot().getAccountId()).isEqualTo(accountId);
        assertThat(result.get().getSnapshot().getEmail()).isEqualTo(accountEmail);
    }

    @Test
    void should_return_false_if_email_not_exists() {
        persistAccount(AccountSnapshotMother.create().build());

        boolean result = accountQueryRepositoryAdapter.existsByEmail(new Email("free@email.com"));

        assertThat(result).isFalse();
    }

    private Account persistAccount(final AccountSnapshot snapshot) {
        return accountRepositoryAdapter.save(
                Account.restore(snapshot)
        );
    }
}