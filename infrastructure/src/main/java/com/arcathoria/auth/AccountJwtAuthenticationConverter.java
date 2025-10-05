package com.arcathoria.auth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.UUID;

class AccountJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ACCOUNT_ID_CLAIM = "id";

    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        final String accountId = source.getClaimAsString(ACCOUNT_ID_CLAIM);
        if (accountId == null) {
            throw new BadCredentialsException("Missing account identifier in token");
        }

        final UUID id;
        try {
            id = UUID.fromString(accountId);
        } catch (IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid account identifier in token", ex);
        }

        final Collection<GrantedAuthority> authorities = authoritiesConverter.convert(source);
        final AccountPrincipal principal = new AccountPrincipal(id);

        return new AccountAuthenticationToken(
                principal,
                source,
                authorities != null ? authorities : java.util.List.of()
        );
    }

    private static final class AccountAuthenticationToken extends AbstractAuthenticationToken {

        private final AccountPrincipal principal;
        private final Jwt jwt;

        private AccountAuthenticationToken(
                final AccountPrincipal principal,
                final Jwt jwt,
                final Collection<? extends GrantedAuthority> authorities
        ) {
            super(authorities);
            this.principal = principal;
            this.jwt = jwt;
            setAuthenticated(true);
            setDetails(jwt);
        }

        @Override
        public Object getCredentials() {
            return jwt.getTokenValue();
        }

        @Override
        public AccountPrincipal getPrincipal() {
            return principal;
        }
    }
}
