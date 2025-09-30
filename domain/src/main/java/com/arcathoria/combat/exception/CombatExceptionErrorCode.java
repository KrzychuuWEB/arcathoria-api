package com.arcathoria.combat.exception;

import com.arcathoria.exception.DomainErrorCode;
import com.arcathoria.exception.DomainExceptionCodeCategory;

public enum CombatExceptionErrorCode implements DomainErrorCode {

    ERR_COMBAT_WRONG_TURN(DomainExceptionCodeCategory.CONFLICT),
    ERR_COMBAT_ACTION_TYPE(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_COMBAT_PARTICIPANT_NOT_FOUND_IN_COMBAT(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_COMBAT_ONLY_ONE_ACTIVE_COMBAT(DomainExceptionCodeCategory.CONFLICT),
    ERR_COMBAT_ALREADY_FINISHED(DomainExceptionCodeCategory.CONFLICT),
    ERR_PARTICIPANT_NOT_HAS_ACTIVE_COMBAT(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_COMBAT_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND),
    ERR_PARTICIPANT_NOT_FOUND(DomainExceptionCodeCategory.NOT_FOUND);

    private final DomainExceptionCodeCategory category;

    CombatExceptionErrorCode(final DomainExceptionCodeCategory category) {
        this.category = category;
    }

    @Override
    public DomainExceptionCodeCategory getCategory() {
        return category;
    }

    @Override
    public String getCodeName() {
        return name();
    }
}
