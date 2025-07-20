package com.arcathoria.character.command;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

public record SelectCharacterCommand(
        AccountId accountId,
        CharacterId characterId
) {
}
