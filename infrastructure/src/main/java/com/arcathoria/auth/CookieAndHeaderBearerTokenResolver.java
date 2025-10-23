package com.arcathoria.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.StringUtils;

import java.util.Arrays;

class CookieAndHeaderBearerTokenResolver implements BearerTokenResolver {

    static final String SESSION_COOKIE_NAME = "session";

    private final DefaultBearerTokenResolver defaultBearerTokenResolver;
    private final String cookieName;

    CookieAndHeaderBearerTokenResolver() {
        this(SESSION_COOKIE_NAME);
    }

    CookieAndHeaderBearerTokenResolver(final String cookieName) {
        this.cookieName = cookieName;
        this.defaultBearerTokenResolver = new DefaultBearerTokenResolver();
    }

    @Override
    public String resolve(final HttpServletRequest request) {
        final String bearerToken = defaultBearerTokenResolver.resolve(request);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(null);
    }
}
