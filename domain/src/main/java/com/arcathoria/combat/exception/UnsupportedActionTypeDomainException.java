package com.arcathoria.combat.exception;

import com.arcathoria.combat.ActionType;

import java.util.Map;

public class UnsupportedActionTypeDomainException extends CombatDomainException {

    private final ActionType actionType;

    public UnsupportedActionTypeDomainException(final ActionType actionType) {
        super("This type of action is not supported: " +
                        actionType, CombatExceptionErrorCode.ERR_COMBAT_ACTION_TYPE,
                Map.of("actionType", actionType.name())
        );
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
