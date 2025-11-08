package com.arcathoria.character;

final class CharacterOpenApiExamples {

    private CharacterOpenApiExamples() {
    }

    static final String ACCOUNT_SERVICE_UNAVAILABLE = """
            {
               "type":"urn:arcathoria:character:err-service-unavailable",
               "title":"ERR SERVICE UNAVAILABLE",
               "status":503,
               "detail":"Service temporarily unavailable. Please try again later.",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_SERVICE_UNAVAILABLE",
               "context":{
                  "service":"account"
               }
            }
            """;

    static final String CHARACTER_OWNER_NOT_FOUND = """
            {
               "type":"urn:arcathoria:character:err-character-owner-not-found",
               "title":"ERR CHARACTER OWNER NOT FOUND",
               "status":404,
               "detail":"Owner with id {id} not found",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_CHARACTER_OWNER_NOT_FOUND",
               "context":{
                  "accountId":"UUID"
               },
               "upstream":{
                  "service":"account",
                  "code":"ERR_ACCOUNT_NOT_FOUND"
               }
            }
            """;

    static final String CHARACTER_NOT_FOUND = """
            {
               "type":"urn:arcathoria:character:err-character-not-found",
               "title":"ERR CHARACTER NOT FOUND",
               "status":404,
               "detail":"Character with id {id} not found!",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_CHARACTER_NOT_FOUND",
               "context":{
                  "characterId":"UUID"
               }
            }
            """;

    static final String CHARACTER_NOT_OWNED = """
            {
               "type":"urn:arcathoria:character:err-character-not-owned",
               "title":"ERR CHARACTER NOT OWNED",
               "status":403,
               "detail":"Character with id {id} is not owned by the authenticated account",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_CHARACTER_NOT_OWNED",
               "context":{
                  "characterId":"UUID"
               }
            }
            """;

    static final String CHARACTER_NOT_SELECTED = """
            {
               "type":"urn:arcathoria:character:err-character-not-selected",
               "title":"ERR CHARACTER NOT SELECTED",
               "status":404,
               "detail":"Character not selected for account {id}",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_CHARACTER_NOT_SELECTED",
               "context":{
                  "accountId":"UUID"
               }
            }
            """;

    static final String CHARACTER_NAME_EXISTS = """
            {
               "type":"urn:arcathoria:character:err-character-name-exists",
               "title":"ERR CHARACTER NAME EXISTS",
               "status":409,
               "detail":"Character name {character_name} already exists",
               "instance":"/v1/characters/*",
               "errorCode":"ERR_CHARACTER_NAME_EXISTS",
               "context":{
                  "characterName":"character_name"
               }
            }
            """;
}
