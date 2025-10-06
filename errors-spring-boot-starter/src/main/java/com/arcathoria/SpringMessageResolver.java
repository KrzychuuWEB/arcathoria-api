package com.arcathoria;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

public class SpringMessageResolver implements MessageResolver {

    private final MessageSource messageSource;

    public SpringMessageResolver(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String resolve(final String key, final Map<String, Object> args, final String defaultMessage, final Locale locale) {
        return messageSource.getMessage(key,
                args.values().toArray(),
                defaultMessage,
                locale
        );
    }
}
