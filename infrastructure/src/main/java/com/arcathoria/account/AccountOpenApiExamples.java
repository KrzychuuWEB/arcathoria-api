package com.arcathoria.account;

final class AccountOpenApiExamples {

    private AccountOpenApiExamples() {
    }

    public static final String EMAIL_EXISTS = """
            {
               "type":"urn:arcathoria:account:err-account-email-exists",
               "title":"ERR ACCOUNT EMAIL EXISTS",
               "status":409,
               "detail":"Account with this email already exists.",
               "instance":"/v1/accounts/register",
               "errorCode":"ERR_ACCOUNT_EMAIL_EXISTS",
               "context":{
                  "email":"email@example.com"
               }
            }
            """;
}


