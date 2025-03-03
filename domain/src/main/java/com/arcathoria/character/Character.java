package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class Character {

    static Character restore(CharacterSnapshot snapshot) {
        return new Character(
                snapshot.getCharacterId(),
                snapshot.getName(),
                snapshot.getAccountId()
        );
    }

    private final CharacterId characterId;
    private final CharacterName name;
    private final AccountId accountId;

    private Character(final CharacterId characterId, final CharacterName name, final AccountId accountId) {
        this.characterId = characterId;
        this.name = name;
        this.accountId = accountId;
    }

    CharacterSnapshot getSnapshot() {
        return new CharacterSnapshot(
                characterId,
                name,
                accountId
        );
    }
}
