package com.arcathoria.account;

import com.arcathoria.IntegrationTestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AccountRepositoryAdapter.class)
class AccountRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Test
    void should_save_account() {
        Account account = Account.restore(AccountSnapshotMother.create().build());
        Account result = accountRepositoryAdapter.save(account);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getAccountId().value()).isNotNull();
        assertThat(result.getSnapshot().getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }
}