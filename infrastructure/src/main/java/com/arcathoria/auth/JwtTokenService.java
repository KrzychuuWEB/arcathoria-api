package com.arcathoria.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtTokenService {

    private final JwtConfigurationProperties properties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    JwtTokenService(final JwtConfigurationProperties properties,
                    final JwtEncoder jwtEncoder,
                    final JwtDecoder jwtDecoder) {
        this.properties = properties;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(final String email, final UUID id) {
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

    boolean validateToken(final String token, final UserDetails userDetails) {
        Jwt jwt = jwtDecoder.decode(token);
        return userDetails.getUsername().equals(jwt.getSubject());
    }

    String extractUserName(final String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
