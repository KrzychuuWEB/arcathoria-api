package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;

class Character {

    static Character restore(CharacterSnapshot snapshot) {
        return new Character(
                snapshot.getCharacterId(),
                snapshot.getAccountId(),
                snapshot.getCharacterName(),
                snapshot.getHealth(),
                snapshot.getAttributes()
        );
    }

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;
    private final Health health;
    private final Attributes attributes;

    private Character(
            final CharacterId characterId,
            final AccountId accountId,
            final CharacterName characterName,
            final Health health,
            final Attributes attributes
    ) {
        this.characterId = characterId;
        this.accountId = accountId;
        this.characterName = characterName;
        this.health = health;
        this.attributes = attributes;
    }

    CharacterSnapshot getSnapshot() {
        return new CharacterSnapshot(
                characterId,
                accountId,
                characterName,
                health,
                attributes
        );
    }
}
