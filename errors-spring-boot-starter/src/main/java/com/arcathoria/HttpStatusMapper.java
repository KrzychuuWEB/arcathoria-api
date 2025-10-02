package com.arcathoria;

import com.arcathoria.exception.DomainExceptionCodeCategory;
import org.springframework.http.HttpStatus;

public interface HttpStatusMapper {

    HttpStatus toHttpStatus(final DomainExceptionCodeCategory category);
}
