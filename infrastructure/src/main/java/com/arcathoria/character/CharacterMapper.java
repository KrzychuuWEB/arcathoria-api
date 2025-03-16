package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

class CharacterMapper {

    CharacterMapper() {
    }

    static Character toDomain(CharacterEntity entity) {
        return Character.restore(
                new CharacterSnapshot(
                        new CharacterId(entity.getId()),
                        new AccountId(entity.getAccountId()),
                        new CharacterName(entity.getName())
                )
        );
    }

    static CharacterEntity toEntity(Character character) {
        return new CharacterEntity(
                character.getSnapshot().getCharacterId().value(),
                character.getSnapshot().getAccountId().value(),
                character.getSnapshot().getCharacterName().value()
        );
    }
}
