package com.arcathoria.autoconfigure;

import com.arcathoria.HttpStatusMapper;
import com.arcathoria.MessageResolver;
import com.arcathoria.ProblemDetailsFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ErrorAutoConfiguration.class))
            .withBean(MessageSource.class, StaticMessageSource::new);

    @Test
    void should_auto_configure_all_beans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MessageResolver.class);
            assertThat(context).hasSingleBean(HttpStatusMapper.class);
            assertThat(context).hasSingleBean(ProblemDetailsFactory.class);
        });
    }

    @Test
    void should_use_default_implementations_when_not_configured_by_user() {
        contextRunner.run(context -> {
            assertThat(context.getBean(MessageResolver.class))
                    .isInstanceOf(com.arcathoria.SpringMessageResolver.class);

            assertThat(context.getBean(HttpStatusMapper.class))
                    .isInstanceOf(com.arcathoria.DefaultHttpStatusMapper.class);
        });
    }

    @Test
    void should_not_auto_configure_when_message_source_is_missing() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ErrorAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MessageResolver.class);
                    assertThat(context).doesNotHaveBean(ProblemDetailsFactory.class);
                });
    }
}