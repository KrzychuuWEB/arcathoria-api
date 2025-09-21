package com.arcathoria.combat.exception;

import com.arcathoria.DomainException;
import com.arcathoria.combat.ActionType;

public class UnsupportedActionTypeException extends DomainException {

    private final ActionType actionType;

    public UnsupportedActionTypeException(final ActionType actionType) {
        super("This type of action is not supported: " + actionType, "ERR_COMBAT_ACTION_TYPE");
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
