package com.arcathoria.combat.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class CombatParticipantUnavailableException extends ApiException {

    private final UUID id;

    public CombatParticipantUnavailableException(final UUID id) {
        super("Could not retrieve participant for combat.", "ERR_COMBAT_PARTICIPANT_UNAVAILABLE-400");
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
