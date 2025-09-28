package com.arcathoria.auth;

import org.springframework.http.HttpHeaders;

public class GetJwtTokenHelper {

    public static String extractToken(final HttpHeaders headers) {
        return headers.getFirst(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
    }
}
