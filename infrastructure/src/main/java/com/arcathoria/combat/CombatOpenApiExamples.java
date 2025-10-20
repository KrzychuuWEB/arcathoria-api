package com.arcathoria.combat;

final class CombatOpenApiExamples {

    private CombatOpenApiExamples() {
    }

    static final String COMBAT_NOT_FOUND = """
            {
               "type":"urn:arcathoria:combat:err-combat-not-found",
               "title":"ERR COMBAT NOT FOUND",
               "status":404,
               "detail":"Combat not found with id: {id}",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_NOT_FOUND",
               "context":{
                  "combatId":"UUID"
               }
            }
            """;

    static final String PARTICIPANT_CHARACTER_NOT_FOUND = """
            {
               "type":"urn:arcathoria:combat:err-combat-participant-not-available",
               "title":"ERR COMBAT PARTICIPANT NOT AVAILABLE",
               "status":404,
               "detail":"Participant with id {id} not found",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE",
               "context":{
                  "participantId":"UUID"
               },
               "upstream":{
                  "service":"character",
                  "code":"ERR_CHARACTER_NOT_FOUND || ERR_CHARACTER_NOT_SELECTED"
               }
            }
            """;

    static final String PARTICIPANT_MONSTER_NOT_FOUND = """
            {
               "type":"urn:arcathoria:combat:err-combat-participant-not-available",
               "title":"ERR COMBAT PARTICIPANT NOT AVAILABLE",
               "status":404,
               "detail":"Participant with id {id} not found",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE",
               "context":{
                  "participantId":"UUID"
               },
               "upstream":{
                  "service":"monster",
                  "code":"ERR_MONSTER_NOT_FOUND"
               }
            }
            """;

    static final String CHARACTER_SERVICE_UNAVAILABLE = """
            {
                "type":"urn:arcathoria:combat:err-service-unavailable",
                "title":"ERR SERVICE UNAVAILABLE",
                "status":503,
                "detail":"Service temporarily unavailable. Please try again later.",
                "instance":"/v1/combats/*",
                "errorCode":"ERR_SERVICE_UNAVAILABLE",
                "context":{
                   "service":"character"
                }
             }
            """;

    static final String MONSTER_SERVICE_UNAVAILABLE = """
            {
                "type":"urn:arcathoria:combat:err-combat-service-unavailable",
                "title":"ERR SERVICE UNAVAILABLE",
                "status":503,
                "detail":"Service temporarily unavailable. Please try again later.",
                "instance":"/v1/combats/*",
                "errorCode":"ERR_SERVICE_UNAVAILABLE",
                "context":{
                   "service":"monster"
                }
             }
            """;

    static final String PARTICIPANT_NOT_HAS_ACTIVE_COMBAT = """
            {
               "type":"urn:arcathoria:combat:err-pariticpant-not-has-active-combat",
               "title":"ERR PARTICIPANT NOT HAS ACTIVE COMBAT",
               "status":404,
               "detail":"Participant {id} not has active combats.",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT",
               "context":{
                  "participantId":"UUID"
               }
            }
            """;

    static final String COMBAT_ALREADY_FINISHED = """
            {
               "type":"urn:arcathoria:combat:err-combat-already-finished",
               "title":"ERR COMBAT ALREADY FINISHED",
               "status":409,
               "detail":"The fight for id {id} is already finished, this action cannot be performed",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_ALREADY_FINISHED",
               "context":{
                  "combatId":"UUID"
               }
            }
            """;

    static final String ONLY_ONE_ACTIVE_COMBAT = """
            {
               "type":"urn:arcathoria:combat:err-only-one-active-combat",
               "title":"ERR ONLY ONE ACTIVE COMBAT",
               "status":409,
               "detail":"Participant {id} already has an active combat",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_ONLY_ONE_ACTIVE_COMBAT",
               "context":{
                  "participantId":"UUID"
               }
            }
            """;

    static final String PARTICIPANT_NOT_FOUND_IN_COMBAT = """
            {
               "type":"urn:arcathoria:combat:err-combat-participant-not-found-in-combat",
               "title":"ERR COMBAT PARTICIPANT NOT FOUND IN COMBAT",
               "status":404,
               "detail":"Participant with id {participantId} not found in combat {combatId}",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_PARTICIPANT_NOT_FOUND_IN_COMBAT",
               "context":{
                  "combatId":"UUID",
                  "participantId":"UUID"
               }
            }
            """;

    static final String ACTION_TYPE_NOT_FOUND = """
            {
               "type":"urn:arcathoria:combat:err-combat-action-type-not-found",
               "title":"ERR COMBAT ACTION TYPE NOT FOUND",
               "status":404,
               "detail":"This type of action is not supported: {type}",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_ACTION_TYPE_NOT_FOUND",
               "context":{
                  "actionType":"string (MELEE)"
               }
            }
            """;

    static final String WRONG_TURN = """
            {
               "type":"urn:arcathoria:combat:err-combat-wrong-turn",
               "title":"ERR COMBAT WRONG TURN",
               "status":409,
               "detail":"Turn belongs to {combatSide} you cannot perform the action now",
               "instance":"/v1/combats/*",
               "errorCode":"ERR_COMBAT_WRONG_TURN",
               "context":{
                  "combatSide":"ATTACKER || DEFENDER"
               }
            }
            """;
}
