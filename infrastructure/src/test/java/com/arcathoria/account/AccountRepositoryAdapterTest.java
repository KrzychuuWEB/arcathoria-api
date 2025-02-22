package com.arcathoria.account;

import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
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
    void should_save_and_find_account_by_email() {
        Account domain = Account.restore(new AccountSnapshot(
                new AccountId(null),
                new Email("test@email.com"),
                new HashedPassword("secret_password")
        ));
        accountRepositoryAdapter.save(domain);

        Optional<Account> result = accountRepositoryAdapter.findByEmail(domain.getSnapshot().getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getSnapshot().getAccountId().getValue()).isNotNull();
        assertThat(result.get().getSnapshot().getEmail().getValue()).isEqualTo(domain.getSnapshot().getEmail().getValue());
        assertThat(result.get().getSnapshot().getPassword().getValue()).isEqualTo(domain.getSnapshot().getPassword().getValue());
    }
}