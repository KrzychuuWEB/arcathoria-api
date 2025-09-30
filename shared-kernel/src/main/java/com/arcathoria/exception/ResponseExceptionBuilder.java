package com.arcathoria.exception;

import java.util.Locale;

public final class ResponseExceptionBuilder {

    public static String generateType(final String domain, final DomainErrorCode domainErrorCode) {
        return "urn:arcathoria:" + domain + ":" + domainErrorCode.getCodeName().replace('_', '-').toLowerCase(Locale.ROOT);
    }

    public static String generateTitle(final DomainErrorCode domainErrorCode) {
        return domainErrorCode.getCodeName().replace('_', ' ').toUpperCase();
    }

    public static String generateKeyWithDots(final String domain, final DomainErrorCode domainErrorCode) {
        return domain + "." + domainErrorCode.getCodeName().replace('_', '.');
    }
}
