package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;

class CharacterSnapshot {

    private final CharacterId characterId;
    private final AccountId accountId;
    private final CharacterName characterName;
    private final Health health;
    private final Attributes attributes;

    CharacterSnapshot(
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

    Attributes getAttributes() {
        return attributes;
    }
}
