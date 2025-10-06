package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.junit.jupiter.api.Test;

import static com.arcathoria.ErrorKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

class ErrorKeysTest {

    @Test
    void should_generate_valid_type_key() {
        String result = generateType("test", TestErrorCode.TEST_NOT_FOUND);

        assertThat(result).isEqualTo("urn:arcathoria:test:test-not-found");
    }

    @Test
    void should_generate_valid_title_key() {
        String result = generateTitle(TestErrorCode.TEST_NOT_FOUND);

        assertThat(result).isEqualTo("TEST NOT FOUND");
    }

    @Test
    void should_generate_valid_key_with_dots() {
        String result = generateKeyWithDots("test", TestErrorCode.TEST_NOT_FOUND);

        assertThat(result).isEqualTo("test.test.not.found");
    }

    private enum TestErrorCode implements DomainErrorCode {
        TEST_NOT_FOUND;


        @Override
        public DomainExceptionCodeCategory getCategory() {
            return null;
        }

        @Override
        public String getCodeName() {
            return name();
        }
    }
}