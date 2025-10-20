package com.arcathoria;

import com.arcathoria.exception.UpstreamInfo;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class GenericOpenApiProblemDetail<E extends Enum<E>> extends ProblemDetail {

    private E errorCode;

    private Map<String, Object> context;

    private UpstreamInfo upstream;

    public E getErrorCode() {
        return errorCode;
    }

    void setErrorCode(final E errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    void setContext(final Map<String, Object> context) {
        this.context = context;
    }

    public UpstreamInfo getUpstream() {
        return upstream;
    }

    void setUpstream(final UpstreamInfo upstream) {
        this.upstream = upstream;
    }
}
