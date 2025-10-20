package com.arcathoria.combat.exception;

import com.arcathoria.combat.ActionType;

import java.util.Map;

public class UnsupportedActionTypeException extends CombatDomainException {

    private final ActionType actionType;

    public UnsupportedActionTypeException(final ActionType actionType) {
        super("This type of action is not supported: " + actionType,
                CombatExceptionErrorCode.ERR_COMBAT_ACTION_TYPE_NOT_FOUND,
                Map.of("actionType", actionType.name())
        );
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
