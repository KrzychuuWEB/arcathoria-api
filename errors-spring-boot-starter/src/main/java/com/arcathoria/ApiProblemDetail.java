package com.arcathoria;

import com.arcathoria.exception.UpstreamInfo;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class ApiProblemDetail extends ProblemDetail {

    private String errorCode;

    private Map<String, Object> context;

    private UpstreamInfo upstream;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(final Map<String, Object> context) {
        this.context = context;
    }

    public UpstreamInfo getUpstream() {
        return upstream;
    }

    public void setUpstream(final UpstreamInfo upstream) {
        this.upstream = upstream;
    }
}
