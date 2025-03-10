package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class Character {

    static Character restore(CharacterSnapshot snapshot) {
        return new Character(
                snapshot.getCharacterId(),
                snapshot.getAccountId(),
                snapshot.getName()
        );
    }

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName name;

    private Character(final CharacterId characterId, final AccountId accountId, final CharacterName name) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.name = name;
    }

    CharacterSnapshot getSnapshot() {
        return new CharacterSnapshot(
                characterId,
                accountId,
                name
        );
    }
}
