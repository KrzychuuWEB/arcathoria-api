package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class CharacterSnapshot {

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName name;

    CharacterSnapshot(final CharacterId characterId, final AccountId accountId, final CharacterName name) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.name = name;
    }

    CharacterId getCharacterId() {
        return characterId;
    }

    AccountId getAccountId() {
        return accountId;
    }

    CharacterName getName() {
        return name;
    }
}
