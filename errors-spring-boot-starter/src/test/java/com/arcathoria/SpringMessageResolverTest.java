package com.arcathoria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringMessageResolverTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SpringMessageResolver springMessageResolver;

    @Test
    void should_return_correct_message_with_context() {
        String key = "test.message.with.context";
        Map<String, Object> context = Map.of("name", "correct_context");
        String defaultMessage = "default test message";
        Locale locale = Locale.ENGLISH;
        String message = "Test message with name: " + context.get("name");

        when(messageSource.getMessage(eq(key), any(), eq(defaultMessage), eq(locale)))
                .thenReturn(message);

        String result = springMessageResolver.resolve(key, context, defaultMessage, locale);

        assertThat(result).isEqualTo(message);
        verify(messageSource).getMessage(
                eq(key),
                aryEq(new Object[]{"correct_context"}),
                eq(defaultMessage),
                eq(locale)
        );
    }

    @Test
    void should_return_default_message_when_key_not_found() {
        String key = "test.message.key.not.found";
        Map<String, Object> context = Map.of();
        String defaultMessage = "default test message";

        when(messageSource.getMessage(eq(key), any(), eq(defaultMessage), any()))
                .thenReturn(defaultMessage);

        String result = springMessageResolver.resolve(key, context, defaultMessage, Locale.ENGLISH);

        assertThat(result).isEqualTo(defaultMessage);
    }

    @Test
    void should_return_message_without_context() {
        String key = "test.message.without.context";
        String defaultMessage = "default test message";
        Locale locale = Locale.ENGLISH;
        String message = "test message without context";

        when(messageSource.getMessage(eq(key), any(), eq(defaultMessage), eq(locale)))
                .thenReturn(message);

        String result = springMessageResolver.resolve(key, Map.of(), defaultMessage, locale);

        assertThat(result).isEqualTo(message);
        verify(messageSource).getMessage(
                eq(key),
                eq(new Object[]{}),
                eq(defaultMessage),
                eq(locale)
        );
    }
}