package com.arcathoria.character;

import com.arcathoria.BaseFakeClient;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFound;
import com.arcathoria.character.exception.ExternalServiceUnavailableException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Primary
@Profile("test")
class FakeAccountClient extends BaseFakeClient<UUID, AccountView> implements AccountClient {

    @Override
    public AccountView getById(final AccountId accountId) {
        return get(accountId.value(), (id, errorCode) -> new CharacterOwnerNotFound(
                new AccountId(id),
                new UpstreamInfo("account", errorCode)
        ));
    }

    public FakeAccountClient withAccount(final UUID account, final AccountView accountView) {
        data.put(account, accountView);
        return this;
    }

    public FakeAccountClient withDefaultAccount(final UUID accountId) {
        data.put(accountId, new AccountView(accountId));
        return this;
    }

    public FakeAccountClient throwCharacterOwnerNotFoundException() {
        configureException("NOT_FOUND");
        return this;
    }

    public FakeAccountClient throwExternalServiceException() {
        this.shouldThrowException = true;
        configureException("EXTERNAL_SERVICE");
        return this;
    }

    @Override
    protected void throwConfiguredException(final UUID key) {
        switch (exceptionType) {
            case "NOT_FOUND":
                throw new CharacterOwnerNotFound(
                        new AccountId(key),
                        new UpstreamInfo("account", getDefaultNotFoundErrorCode())
                );
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("account");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }

    @Override
    protected String getDefaultNotFoundErrorCode() {
        return "ERR_ACCOUNT_NOT_FOUND";
    }
}
