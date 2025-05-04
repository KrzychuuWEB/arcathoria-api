package com.arcathoria.monster.exception;

import com.arcathoria.ApiException;

public class MonsterNotFoundException extends ApiException {

    private final String monsterId;

    public MonsterNotFoundException(final String monsterId) {
        super("Monster with id " + monsterId + " not found!", "ERR_MONSTER_NOT_FOUND-404");
        this.monsterId = monsterId;
    }

    public String getMonsterId() {
        return monsterId;
    }
}
