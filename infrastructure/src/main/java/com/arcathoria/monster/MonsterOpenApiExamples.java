package com.arcathoria.monster;

final class MonsterOpenApiExamples {

    private MonsterOpenApiExamples() {
    }

    static final String MONSTER_NOT_FOUND = """
            {
               "type":"urn:arcathoria:monster:err-monster-not-found",
               "title":"ERR MONSTER NOT FOUND",
               "status":404,
               "detail":"Monster not found with id {}",
               "instance":"/v1/monsters/{id}",
               "errorCode":"ERR_MONSTER_NOT_FOUND",
               "context":{
                  "monsterId":"UUID"
               }
            }
            """;
}
