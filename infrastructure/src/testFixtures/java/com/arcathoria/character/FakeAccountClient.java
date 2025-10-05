package com.arcathoria.character;

import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFound;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@Profile("test")
@Primary
class FakeAccountClient implements AccountClient {

    private final Map<AccountId, AccountView> accounts = new HashMap<>();
    private boolean shouldThrowException = false;
    private String exceptionType = null;

    @Override
    public AccountView getById(final AccountId accountId) {
        if (shouldThrowException) {
            throwConfiguredException(accountId);
        }

        AccountView accountView = accounts.get(accountId);
        if (accountView == null) {
            throw new CharacterOwnerNotFound(
                    accountId,
                    new UpstreamInfo("account", "ERR_ACCOUNT_NOT_FOUND")
            );
        }
        return accountView;
    }

    public FakeAccountClient withAccount(final AccountId account, final AccountView accountView) {
        accounts.put(account, accountView);
        return this;
    }

    public FakeAccountClient withDefaultAccount(final AccountId accountId) {
        accounts.put(accountId, new AccountView(accountId.value()));
        return this;
    }

    public FakeAccountClient throwCharacterNotFoundException() {
        this.shouldThrowException = true;
        this.exceptionType = "NOT_FOUND";
        return this;
    }

    public FakeAccountClient throwExternalServiceException() {
        this.shouldThrowException = true;
        this.exceptionType = "EXTERNAL_SERVICE";
        return this;
    }

    public void reset() {
        accounts.clear();
        shouldThrowException = false;
        exceptionType = null;
    }

    private void throwConfiguredException(final AccountId accountId) {
        switch (exceptionType) {
            case "NOT_FOUND":
                throw new CharacterOwnerNotFound(
                        accountId,
                        new UpstreamInfo("account", "ERR_ACCOUNT_NOT_FOUND")
                );
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("account");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }
}
