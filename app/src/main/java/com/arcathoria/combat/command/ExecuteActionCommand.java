package com.arcathoria.combat.command;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.ActionType;
import com.arcathoria.combat.vo.CombatId;

public record ExecuteActionCommand(
        CombatId combatId,
        AccountId accountId,
        ActionType actionType
) {
}
