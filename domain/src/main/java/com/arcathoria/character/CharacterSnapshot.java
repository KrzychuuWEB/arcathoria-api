package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class CharacterSnapshot {

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;

    CharacterSnapshot(final CharacterId characterId, final AccountId accountId, final CharacterName characterName) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.characterName = characterName;
    }

    CharacterId getCharacterId() {
        return characterId;
    }

    AccountId getAccountId() {
        return accountId;
    }

    CharacterName getCharacterName() {
        return characterName;
    }
}
