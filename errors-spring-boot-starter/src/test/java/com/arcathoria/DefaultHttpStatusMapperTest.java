package com.arcathoria;

import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultHttpStatusMapperTest {

    private DefaultHttpStatusMapper mapper = new DefaultHttpStatusMapper();

    @ParameterizedTest
    @MethodSource("categoryToStatusMappings")
    void should_return_correct_http_status_when_for_code_category(
            DomainExceptionCodeCategory category,
            HttpStatus expectedHttpStatus
    ) {
        HttpStatus result = mapper.toHttpStatus(category);

        assertEquals(expectedHttpStatus, result);
    }

    static Stream<Arguments> categoryToStatusMappings() {
        return Stream.of(
                Arguments.of(DomainExceptionCodeCategory.NOT_FOUND, HttpStatus.NOT_FOUND),
                Arguments.of(DomainExceptionCodeCategory.CONFLICT, HttpStatus.CONFLICT)
        );
    }
}