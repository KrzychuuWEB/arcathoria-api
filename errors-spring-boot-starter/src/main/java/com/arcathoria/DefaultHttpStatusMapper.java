package com.arcathoria;

import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.springframework.http.HttpStatus;

public class DefaultHttpStatusMapper implements HttpStatusMapper {

    @Override
    public HttpStatus toHttpStatus(final DomainExceptionCodeCategory category) {
        return switch (category) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case SERVICE_UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
