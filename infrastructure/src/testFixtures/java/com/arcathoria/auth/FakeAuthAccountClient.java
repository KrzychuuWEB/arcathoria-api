package com.arcathoria.auth;

import com.arcathoria.BaseFakeClient;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Primary
@Profile("test")
class FakeAuthAccountClient extends BaseFakeClient<String, AccountView> implements AuthAccountClient {

    @Override
    public AccountView validate(final String email, final String password) {
        return get(email, (id, errorCode) -> new AuthBadCredentialsException());
    }

    public FakeAuthAccountClient withAccount(final String email, final AccountView accountView) {
        put(email, accountView);
        return this;
    }

    public FakeAuthAccountClient throwBadCredentialsException() {
        configureException("BAD_CREDENTIALS");
        return this;
    }

    public FakeAuthAccountClient throwExternalServiceException() {
        configureException("EXTERNAL_SERVICE");
        return this;
    }

    @Override
    protected void throwConfiguredException(final String key) {
        switch (exceptionType) {
            case "BAD_CREDENTIALS":
                throw new AuthBadCredentialsException();
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("auth");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }

    @Override
    protected String getDefaultNotFoundErrorCode() {
        return "BAD_CREDENTIALS";
    }
}
