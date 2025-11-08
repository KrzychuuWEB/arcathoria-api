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
        DefaultBearerTokenResolver d = new DefaultBearerTokenResolver();
        d.setAllowUriQueryParameter(false);
        d.setAllowFormEncodedBodyParameter(false);
        this.defaultBearerTokenResolver = d;
    }

    @Override
    public String resolve(final HttpServletRequest request) {
        String bearer = defaultBearerTokenResolver.resolve(request);
        if (StringUtils.hasText(bearer)) return bearer;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(null);
    }
}

