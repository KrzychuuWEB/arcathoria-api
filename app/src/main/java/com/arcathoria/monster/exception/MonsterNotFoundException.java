package com.arcathoria.monster.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class MonsterNotFoundException extends ApiException {

    private final UUID monsterId;

    public MonsterNotFoundException(final UUID monsterId) {
        super("Monster with id " + monsterId + " not found!", "ERR_MONSTER_NOT_FOUND-404");
        this.monsterId = monsterId;
    }

    public UUID getMonsterId() {
        return monsterId;
    }
}
