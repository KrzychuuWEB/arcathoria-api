package com.arcathoria;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Locale;

public class SetLocaleHelper {

    public static HttpHeaders withLocale(HttpHeaders headers, String langTag) {
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(langTag)));
        return headers;
    }
}
