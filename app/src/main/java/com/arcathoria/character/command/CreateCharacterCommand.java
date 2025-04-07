package com.arcathoria.character.command;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterName;

public record CreateCharacterCommand(
        AccountId accountId,
        CharacterName characterName
) {
}
