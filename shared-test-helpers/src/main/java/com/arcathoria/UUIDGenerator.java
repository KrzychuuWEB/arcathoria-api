package com.arcathoria;

import java.util.UUID;

public final class UUIDGenerator {

    UUIDGenerator() {
    }

    public static String generate(final Integer max) {
        Integer endIndex = max;
        if (endIndex > 32) {
            endIndex = 32;
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, endIndex);
    }
}
