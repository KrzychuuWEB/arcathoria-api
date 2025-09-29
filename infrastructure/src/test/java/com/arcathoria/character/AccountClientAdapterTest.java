package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.vo.AccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static com.arcathoria.account.JwtTokenDecodeHelper.extractAccountId;
import static com.arcathoria.auth.GetJwtTokenHelper.extractToken;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountClientAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountClientAdapter accountClientAdapter;

    private AccountManagerE2EHelper accountManagerE2EHelper;

    @BeforeEach
    void setup() {
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
    }

    @Test
    void should_get_account_by_account_id() {
        HttpHeaders account = accountManagerE2EHelper.registerAndGetAuthHeaders("email" + UUIDGenerator.generate(5) + "@arcathoria.com");
        AccountId accountId = new AccountId(
                UUID.fromString(
                        extractAccountId(extractToken(account))
                )
        );

        AccountView result = accountClientAdapter.getById(accountId);

        assertThat(result.accountId()).isEqualTo(accountId.value());
    }
}