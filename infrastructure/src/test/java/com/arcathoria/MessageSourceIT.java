package com.arcathoria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageSourceIT extends IntegrationTestContainersConfig {

    @Autowired
    private MessageSource messageSource;

    @Test
    void should_load_message_from_property_file_default_lang() {
        String message = messageSource.getMessage("validation.constraints.AssertFalse", null, Locale.ENGLISH);
        assertThat(message).isEqualTo("Must be false");
    }

    @Test
    void should_load_message_from_property_file_polish_lang() {
        String message = messageSource.getMessage("validation.constraints.AssertFalse", null, Locale.forLanguageTag("pl"));
        assertThat(message).isEqualTo("Musi być fałszem");
    }
}
