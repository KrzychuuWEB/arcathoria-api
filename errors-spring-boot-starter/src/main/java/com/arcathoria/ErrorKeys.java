package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;

import java.util.Locale;

final class ErrorKeys {

    private ErrorKeys() {
    }

    public static String generateType(final String domain, final DomainErrorCode domainErrorCode) {
        return "urn:arcathoria:" + domain + ":" + domainErrorCode.getCodeName().replace('_', '-').toLowerCase(Locale.ROOT);
    }

    public static String generateTitle(final DomainErrorCode domainErrorCode) {
        return domainErrorCode.getCodeName().replace('_', ' ').toUpperCase();
    }

    public static String generateKeyWithDots(final String domain, final DomainErrorCode domainErrorCode) {
        return domain + "." + domainErrorCode.getCodeName().replace('_', '.').toLowerCase(Locale.ROOT);
    }
}
