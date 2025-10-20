package com.arcathoria;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionContract;
import com.arcathoria.exception.UpstreamAware;
import com.arcathoria.exception.UpstreamInfo;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.Locale;

public class ProblemDetailsFactory {

    private final MessageResolver messages;
    private final HttpStatusMapper statusMapper;

    public ProblemDetailsFactory(final MessageResolver messages, final HttpStatusMapper statusMapper) {
        this.messages = messages;
        this.statusMapper = statusMapper;
    }

    public ApiProblemDetail build(final DomainExceptionContract ex,
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

        ApiProblemDetail pd = new ApiProblemDetail();
        pd.setStatus(status.value());
        pd.setDetail(detail);
        pd.setType(URI.create(type));
        pd.setTitle(title);
        pd.setInstance(URI.create(requestUri));
        pd.setErrorCode(code.getCodeName());
        pd.setContext(ex.getContext());

        if (ex instanceof UpstreamAware ua && ua.getUpstreamInfo().isPresent()) {
            var up = ua.getUpstreamInfo().get();
            pd.setUpstream(new UpstreamInfo(up.type(), up.code()));
        }

        ExceptionLogger.log(log, ex, HttpStatus.valueOf(pd.getStatus()));

        return pd;
    }
}
