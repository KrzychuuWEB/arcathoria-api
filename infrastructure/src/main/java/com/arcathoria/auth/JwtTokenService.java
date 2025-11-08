package com.arcathoria.auth;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
class JwtTokenService {

    private final JwtConfigurationProperties properties;
    private final JwtEncoder jwtEncoder;

    JwtTokenService(final JwtConfigurationProperties properties,
                    final JwtEncoder jwtEncoder) {
        this.properties = properties;
        this.jwtEncoder = jwtEncoder;
    }

    String generateToken(final String email, final UUID id) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .subject(email)
                .expiresAt(now.plusSeconds(properties.getValidity()))
                .claim("id", id.toString())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
