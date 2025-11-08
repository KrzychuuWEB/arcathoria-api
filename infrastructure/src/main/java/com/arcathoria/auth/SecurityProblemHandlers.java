package com.arcathoria.auth;

import com.arcathoria.ApiProblemDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
class SecurityProblemHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    SecurityProblemHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(final HttpServletRequest req, final HttpServletResponse res, final AuthenticationException ex)
            throws IOException {
        writeProblem(res, req,
                HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth-err-auth-unauthorized",
                "Authentication Error",
                ex.getMessage(),
                "ERR_AUTH_UNAUTHORIZED");
        res.setHeader("WWW-Authenticate", "Bearer");
    }

    @Override
    public void handle(final HttpServletRequest req, final HttpServletResponse res, final AccessDeniedException ex)
            throws IOException {
        writeProblem(res, req,
                HttpStatus.UNAUTHORIZED,
                "urn:arcathoria:auth-err-auth-access-denied",
                "Access denied Error",
                ex.getMessage(),
                "ERR_AUTH_ACCESS_DENIED");
    }

    private void writeProblem(final HttpServletResponse res,
                              final HttpServletRequest reg,
                              final HttpStatus status,
                              final String type,
                              final String title,
                              final String detail,
                              final String errorCode)
            throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/problem+json;charset=UTF-8");
        objectMapper.writeValue(res.getWriter(), problem(status, type, title, detail, errorCode, reg));
    }

    private ApiProblemDetail problem(final HttpStatus status,
                                     final String type,
                                     final String title,
                                     final String detail,
                                     final String errorCode,
                                     final HttpServletRequest req) {
        ApiProblemDetail pd = new ApiProblemDetail();
        pd.setDetail(detail);
        pd.setStatus(status.value());
        pd.setType(URI.create(type));
        pd.setTitle(title);
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setErrorCode(errorCode);
        return pd;
    }
}
