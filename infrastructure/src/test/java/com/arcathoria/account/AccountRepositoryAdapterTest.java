package com.arcathoria.account;

import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.account.vo.Email;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AccountRepositoryAdapterTest extends PostgreSQLTestContainerConfig {

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Test
    void should_save_account() {
        Account account = Account.restore(AccountSnapshotMother.create().build());
        Account result = accountRepositoryAdapter.save(account);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getAccountId().getValue()).isNotNull();
        assertThat(result.getSnapshot().getEmail().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }

    @Test
    void should_find_account_by_email() {
        Account account = persistAccount(AccountSnapshotMother.create().build());

        Optional<Account> result = accountRepositoryAdapter.findByEmail(account.getSnapshot().getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getSnapshot().getAccountId().getValue()).isNotNull();
        assertThat(result.get().getSnapshot().getEmail().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.get().getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }

    @Test
    void should_return_empty_optional_when_email_not_found() {
        Optional<Account> result = accountRepositoryAdapter.findByEmail(new Email("invalid@email.com"));

        assertThat(result).isEmpty();
    }

    @Test
    void should_return_true_if_email_exists() {
        Email usedEmail = new Email("used@email.com");
        persistAccount(AccountSnapshotMother.create().withAccountId(null).withEmail(usedEmail.getValue()).build());

        boolean result = accountRepositoryAdapter.existsByEmail(usedEmail);

        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_if_email_not_exists() {
        persistAccount(AccountSnapshotMother.create().build());

        boolean result = accountRepositoryAdapter.existsByEmail(new Email("free@email.com"));

        assertThat(result).isFalse();
    }

    private Account persistAccount(AccountSnapshot snapshot) {
        return accountRepositoryAdapter.save(
                Account.restore(snapshot)
        );
    }
}