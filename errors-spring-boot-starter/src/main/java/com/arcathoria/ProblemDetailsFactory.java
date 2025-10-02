package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionContract;
import com.arcathoria.exception.UpstreamAware;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.Locale;
import java.util.Map;

public class ProblemDetailsFactory {

    private final MessageResolver messages;
    private final HttpStatusMapper statusMapper;

    public ProblemDetailsFactory(final MessageResolver messages, final HttpStatusMapper statusMapper) {
        this.messages = messages;
        this.statusMapper = statusMapper;
    }

    public ProblemDetail build(final DomainExceptionContract ex,
                               final String requestUri,
                               final Locale locale,
                               final Logger log
    ) {

        DomainErrorCode code = ex.getErrorCode();
        String key = ErrorKeys.generateKeyWithDots(ex.getDomain(), code);
        String type = ErrorKeys.generateType(ex.getDomain(), code);
        String title = ErrorKeys.generateTitle(code);

        HttpStatus status = statusMapper.toHttpStatus(code.getCategory());
        String fallback = ((Throwable) ex).getMessage();
        String detail = messages.resolve(key, ex.getContext(), fallback, locale);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setType(URI.create(type));
        pd.setTitle(title);
        pd.setInstance(URI.create(requestUri));
        pd.setProperty("errorCode", code.getCodeName());
        pd.setProperty("context", ex.getContext());

        if (ex instanceof UpstreamAware ua && ua.getUpstreamInfo().isPresent()) {
            var up = ua.getUpstreamInfo().get();
            pd.setProperty("upstream", Map.of(
                    "type", up.type(),
                    "code", up.code()
            ));
        }

        ExceptionLogger.log(log, ex, HttpStatus.valueOf(pd.getStatus()));

        return pd;
    }
}
