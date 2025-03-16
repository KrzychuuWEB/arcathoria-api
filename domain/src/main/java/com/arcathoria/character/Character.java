package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class Character {

    static Character restore(CharacterSnapshot snapshot) {
        return new Character(
                snapshot.getCharacterId(),
                snapshot.getAccountId(),
                snapshot.getCharacterName()
        );
    }

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;

    private Character(final CharacterId characterId, final AccountId accountId, final CharacterName characterName) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.characterName = characterName;
    }

    CharacterSnapshot getSnapshot() {
        return new CharacterSnapshot(
                characterId,
                accountId,
                characterName
        );
    }
}
