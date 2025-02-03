package com.arcathoria.account.vo;

import java.util.regex.Pattern;

public class Email {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String value;

    public Email(final String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.value = email;
    }

    private static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public String getValue() {
        return value;
    }
}
