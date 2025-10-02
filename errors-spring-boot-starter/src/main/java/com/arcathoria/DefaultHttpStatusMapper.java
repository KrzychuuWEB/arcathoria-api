package com.arcathoria;

import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.springframework.http.HttpStatus;

public class DefaultHttpStatusMapper implements HttpStatusMapper {

    @Override
    public HttpStatus toHttpStatus(final DomainExceptionCodeCategory category) {
        return switch (category) {
            case DomainExceptionCodeCategory.NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DomainExceptionCodeCategory.CONFLICT -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
