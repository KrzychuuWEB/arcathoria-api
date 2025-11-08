package com.arcathoria.auth;

final class AuthOpenApiExamples {

    AuthOpenApiExamples() {
    }

    static final String BAD_CREDENTIALS = """
            {
               "type":"urn:arcathoria:auth:bad-credentials",
               "title":"ERR AUTH BAD CREDENTIALS",
               "status":401,
               "detail":"Bad credentials",
               "instance":"/v1/authenticate",
               "errorCode":"ERR_AUTH_BAD_CREDENTIALS"
            }
            """;

    public static final String ACCOUNT_SERVICE_UNAVAILABLE = """
            {
               "type":"urn:arcathoria:auth:err-external-service-unavailable",
               "title":"ERR EXTERNAL SERVICE UNAVAILABLE",
               "status":503,
               "detail":"Service temporarily unavailable. Please try again later.",
               "instance":"/v1/authenticate",
               "errorCode":"ERR_EXTERNAL_SERVICE_UNAVAILABLE",
               "context":{
                  "service":"account"
               }
            }
            """;

    public static final String EXPIRED_JWT_TOKEN = """
            {
               "type":"urn:arcathoria:auth:token-expired",
               "title":"ERR AUTH EXPIRED TOKEN",
               "status":401,
               "detail":"Auth token is expired",
               "instance":"/v1/authenticate",
               "errorCode":"ERR_AUTH_EXPIRED_TOKEN"
            }
            """;

    public static final String ACCESS_DENIED = """
            {
               "type":"urn:arcathoria:auth:forbidden",
               "title":"ERR AUTH FORBIDDEN",
               "status":403,
               "detail":"Access denied",
               "instance":"/v1/authenticate",
               "errorCode":"ERR_AUTH_FORBIDDEN"
            }
            """;
}
