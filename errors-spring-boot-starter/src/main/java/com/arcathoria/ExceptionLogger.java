package com.arcathoria;

import com.arcathoria.exception.DomainExceptionContract;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

public final class ExceptionLogger {

    private ExceptionLogger() {
    }

    public static void log(final Logger log, final DomainExceptionContract ex, final HttpStatus status) {
        if (status.is4xxClientError()) {
            log.warn("Domain error: domain={} code={} ctx={}",
                    ex.getDomain(),
                    ex.getErrorCode().getCodeName(),
                    ex.getContext());
        } else {
            log.error("Domain error: domain={} code={} ctx={} ex={}",
                    ex.getDomain(),
                    ex.getErrorCode().getCodeName(),
                    ex.getContext(),
                    (Throwable) ex);
        }
    }
}
