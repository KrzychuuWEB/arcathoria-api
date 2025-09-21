package com.arcathoria.combat.command;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.monster.vo.MonsterId;

public record InitPVECombatCommand(
        AccountId playerId,
        MonsterId monsterId
) {
}
