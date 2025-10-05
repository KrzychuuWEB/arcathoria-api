package com.arcathoria;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AccountWithAuthenticated {

    private final TestJwtTokenGenerator tokenGenerator;

    public AccountWithAuthenticated(final TestJwtTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    public HttpHeaders authenticatedUser(final UUID uuid) {
        HttpHeaders headers = new HttpHeaders();
        String token = tokenGenerator.generateToken(uuid, List.of("ROLE_USER"));
        headers.setBearerAuth(token);

        return headers;
    }

    public HttpHeaders authenticatedWithLangPL(final UUID uuid) {
        HttpHeaders headers = authenticatedUser(uuid);
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("pl")));
        return headers;
    }
}
