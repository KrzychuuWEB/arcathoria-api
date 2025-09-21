package com.arcathoria.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class JwtTokenDecodeHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractAccountId(final String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            return jsonNode.get("id") != null
                    ? jsonNode.get("id").asText()
                    : jsonNode.get("sub").asText();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse JWT payload", e);
        }
    }
}
