package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class CharacterSnapshot {

    private final CharacterId characterId;
    private final CharacterName name;
    private final AccountId accountId;

    CharacterSnapshot(final CharacterId characterId, final CharacterName name, final AccountId accountId) {
        this.characterId = characterId;
        this.name = name;
        this.accountId = accountId;
    }

    CharacterId getCharacterId() {
        return characterId;
    }

    CharacterName getName() {
        return name;
    }

    AccountId getAccountId() {
        return accountId;
    }
}
