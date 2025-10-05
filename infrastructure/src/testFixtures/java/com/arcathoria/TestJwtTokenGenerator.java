package com.arcathoria;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@Primary
@Profile("test")
public class TestJwtTokenGenerator {

    private static final String TEST_SECRET_B64 = "I3poS1pVcm0xTmU0QTUqRG5HSDcwdk1UJjlCWlU3NWU=";

    private final SecretKey secretKey;
    private final JwtEncoder encoder;

    public TestJwtTokenGenerator() {
        byte[] secretBytes = Base64.getDecoder().decode(TEST_SECRET_B64);
        this.secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");

        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    public String generateToken(final UUID userId, final List<String> roles) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("test-issuer")
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("scope", String.join(" ", roles))
                .claim("id", userId.toString())
                .build();

        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}