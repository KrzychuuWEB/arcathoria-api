package com.arcathoria;

import com.arcathoria.exception.DomainExceptionContract;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DomainExceptionHandlerTest.TestController.class)
@Import({
        DomainExceptionHandlerTest.TestConfig.class,
        ProblemDetailsFactory.class,
        SpringMessageResolver.class,
        DefaultHttpStatusMapper.class,
})
class DomainExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Logger logger;

    @MockitoBean
    private MessageSource messageSource;

    @MockitoBean
    private SpringMessageResolver messageResolver;

    @Test
    void should_return_correct_problem_detail_for_exception() throws Exception {
        when(messageResolver.resolve(any(), anyMap(), eq("test message with name: TEST"), any()))
                .thenReturn("test message with name: TEST");

        mockMvc.perform(get("/test-exception").header("Accept-Language", "en"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.type").value("urn:arcathoria:test:test-domain-error-not-found"))
                .andExpect(jsonPath("$.title").value("TEST DOMAIN ERROR NOT FOUND"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("test message with name: TEST"))
                .andExpect(jsonPath("$.errorCode").value("TEST_DOMAIN_ERROR_NOT_FOUND"))
                .andExpect(jsonPath("$.context.name").value("TEST"))
                .andExpect(jsonPath("$.instance").value("/test-exception"));

        verify(logger).warn(
                eq("Domain error: domain={} code={} ctx={}"),
                eq("test"),
                eq("TEST_DOMAIN_ERROR_NOT_FOUND"),
                eq(Map.of("name", "TEST"))
        );
    }

    @Test
    void should_return_message_by_key_for_correct_problem() throws Exception {
        when(messageResolver.resolve(eq("test.test.domain.error.not.found"), anyMap(), anyString(), any()))
                .thenReturn("Custom message from exception by key");

        mockMvc.perform(get("/test-exception").header("Accept-Language", "en"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:arcathoria:test:test-domain-error-not-found"))
                .andExpect(jsonPath("$.title").value("TEST DOMAIN ERROR NOT FOUND"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Custom message from exception by key"))
                .andExpect(jsonPath("$.errorCode").value("TEST_DOMAIN_ERROR_NOT_FOUND"))
                .andExpect(jsonPath("$.context.name").value("TEST"))
                .andExpect(jsonPath("$.instance").value("/test-exception"));

        verify(logger).warn(
                eq("Domain error: domain={} code={} ctx={}"),
                eq("test"),
                eq("TEST_DOMAIN_ERROR_NOT_FOUND"),
                eq(Map.of("name", "TEST"))
        );
    }

    @Configuration
    static class TestConfig {
        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public TestExceptionAdvice testExceptionAdvice(final ProblemDetailsFactory factory, final Logger logger) {
            return new TestExceptionAdvice(factory, logger);
        }
    }

    @RestController
    static class TestController {

        @GetMapping("/test-exception")
        public void throwDomainException() {
            throw new TestDomainException("test message with name: TEST", Map.of("name", "TEST"));
        }
    }

    @RestControllerAdvice
    static class TestExceptionAdvice {
        private final ProblemDetailsFactory factory;
        private final Logger logger;

        TestExceptionAdvice(final ProblemDetailsFactory factory, final Logger logger) {
            this.factory = factory;
            this.logger = logger;
        }

        @ExceptionHandler(TestDomainException.class)
        public ProblemDetail handle(final DomainExceptionContract ex,
                                    final HttpServletRequest request,
                                    final Locale locale) {
            return factory.build(ex, request.getRequestURI(), locale, logger);
        }
    }
}