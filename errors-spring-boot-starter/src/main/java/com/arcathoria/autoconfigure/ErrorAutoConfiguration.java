package com.arcathoria.autoconfigure;

import com.arcathoria.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
class ErrorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpStatusMapper httpStatusMapper() {
        return new DefaultHttpStatusMapper();
    }
    
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(MessageSource.class)
    static class MessageAwareConfiguration {

        @Bean
        @ConditionalOnMissingBean
        MessageResolver messageResolver(MessageSource messageSource) {
            return new SpringMessageResolver(messageSource);
        }

        @Bean
        @ConditionalOnMissingBean
        ProblemDetailsFactory problemDetailsFactory(MessageResolver resolver,
                                                    HttpStatusMapper httpStatusMapper) {
            return new ProblemDetailsFactory(resolver, httpStatusMapper);
        }
    }
}
