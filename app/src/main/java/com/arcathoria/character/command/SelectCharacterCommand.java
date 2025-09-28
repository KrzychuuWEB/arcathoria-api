package com.arcathoria.character.command;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

public record SelectCharacterCommand(
        AccountId accountId,
        CharacterId characterId
) {
}
