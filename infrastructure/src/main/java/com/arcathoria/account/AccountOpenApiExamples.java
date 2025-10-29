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

    public static final String NOT_FOUND = """
            {
               "type":"urn:arcathoria:account:err-account-not-found",
               "title":"ERR ACCOUNT NOT FOUND",
               "status":404,
               "detail":"Account wit this id {id} not found.",
               "instance":"/v1/accounts/me",
               "errorCode":"ERR_ACCOUNT_NOT_FOUND",
               "context":{
                  "accountId":"UUID"
               }
            }
            """;
}


