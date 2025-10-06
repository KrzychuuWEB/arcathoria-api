package com.arcathoria.monster.exception;

import com.arcathoria.monster.vo.MonsterId;

import java.util.Map;

public class MonsterNotFoundException extends MonsterDomainException {

    private final MonsterId monsterId;

    public MonsterNotFoundException(final MonsterId monsterId) {
        super("Monster with id " + monsterId + " not found!",
                MonsterExceptionErrorCode.ERR_MONSTER_NOT_FOUND,
                Map.of("monsterId", monsterId.value())
        );
        this.monsterId = monsterId;
    }

    public MonsterId getMonsterId() {
        return monsterId;
    }
}
