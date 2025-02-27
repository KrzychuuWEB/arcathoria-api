package com.arcathoria;

import java.util.ArrayList;
import java.util.List;

public class ApiErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String errorCode;
    private final String timestamp;
    private final String path;
    private List<ErrorDetail> details;

    public ApiErrorResponse(final int status, final String error, final String message, final String errorCode, final String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.path = path;
        this.details = new ArrayList<>();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public List<ErrorDetail> getDetails() {
        return details;
    }

    public void addDetail(String field, String issue) {
        this.details.add(new ErrorDetail(field, issue));
    }

    public static class ErrorDetail {
        private final String field;
        private final String issue;

        public ErrorDetail(String field, String issue) {
            this.field = field;
            this.issue = issue;
        }

        public String getField() {
            return field;
        }

        public String getIssue() {
            return issue;
        }
    }
}
