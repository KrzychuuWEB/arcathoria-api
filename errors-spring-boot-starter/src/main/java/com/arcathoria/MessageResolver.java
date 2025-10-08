package com.arcathoria;

import java.util.Locale;
import java.util.Map;

public interface MessageResolver {
    String resolve(final String key, final Map<String, Object> args, final String defaultMessage, final Locale locale);
}