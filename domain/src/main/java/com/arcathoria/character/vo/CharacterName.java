package com.arcathoria.character.vo;

import java.util.regex.Pattern;

public class CharacterName {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 25;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9_-]+$";
    private static final Pattern PATTERN = Pattern.compile(VALID_PATTERN);

    private final String value;

    public CharacterName(String value) {
        validate(value);
        this.value = value.trim();
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name is required!");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("The character name required minimum " + MIN_LENGTH + " characters.");
        }
        if (trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("The character name required maximum " + MAX_LENGTH + " characters.");
        }
        if (!PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("The character name must have only letters, numbers, dash (-) and underline (_).");
        }
    }

    public String getValue() {
        return value;
    }
}
