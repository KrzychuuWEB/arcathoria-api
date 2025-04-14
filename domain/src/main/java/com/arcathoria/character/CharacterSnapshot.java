package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.character.vo.Health;

class CharacterSnapshot {

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;
    private final Health health;

    CharacterSnapshot(
            final CharacterId characterId,
            final AccountId accountId,
            final CharacterName characterName,
            final Health health
    ) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.characterName = characterName;
        this.health = health;
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

    Health getHealth() {
        return health;
    }
}
