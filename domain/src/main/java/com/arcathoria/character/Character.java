package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.character.vo.Health;

class Character {

    static Character restore(CharacterSnapshot snapshot) {
        return new Character(
                snapshot.getCharacterId(),
                snapshot.getAccountId(),
                snapshot.getCharacterName(),
                snapshot.getHealth()
        );
    }

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;
    private final Health health;

    private Character(
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

    CharacterSnapshot getSnapshot() {
        return new CharacterSnapshot(
                characterId,
                accountId,
                characterName,
                health
        );
    }
}
