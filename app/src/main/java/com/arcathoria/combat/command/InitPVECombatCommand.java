package com.arcathoria.combat.command;

import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.MonsterId;

public record InitPVECombatCommand(
        AccountId playerId,
        MonsterId monsterId
) {
}
