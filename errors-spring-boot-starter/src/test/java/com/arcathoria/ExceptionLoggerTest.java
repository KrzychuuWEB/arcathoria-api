package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainException;
import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExceptionLoggerTest {

    @Mock
    private Logger logger;

    @Test
    void should_return_correct_log_message_for_4xx_status_code() {
        ExceptionLogger.log(logger, new TestException("Test exception", TestException.TestErrorCode.TEST_NOT_FOUND, null), HttpStatus.NOT_FOUND);

        verify(logger).warn(
                eq("Domain error: domain={} code={} ctx={}"),
                eq("test"),
                eq("TEST_NOT_FOUND"),
                eq(Map.of())
        );
    }

    @Test
    void should_return_correct_log_message_for_5xx_status_code() {
        DomainException domainException = new TestException("Test exception", TestException.TestErrorCode.TEST_NOT_FOUND, null);
        ExceptionLogger.log(logger, domainException, HttpStatus.INTERNAL_SERVER_ERROR);

        verify(logger).error(
                eq("Domain error: domain={} code={} ctx={} ex={}"),
                eq("test"),
                eq("TEST_NOT_FOUND"),
                eq(Map.of()),
                eq(domainException)
        );
    }

    private class TestException extends DomainException {

        TestException(final String message, final DomainErrorCode errorCode, final Map<String, Object> context) {
            super(message, "test", errorCode, context);
        }

        private enum TestErrorCode implements DomainErrorCode {
            TEST_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND);

            private final DomainExceptionCodeCategory category;

            TestErrorCode(final DomainExceptionCodeCategory category) {
                this.category = category;
            }

            @Override
            public DomainExceptionCodeCategory getCategory() {
                return category;
            }

            @Override
            public String getCodeName() {
                return name();
            }
        }
    }
}