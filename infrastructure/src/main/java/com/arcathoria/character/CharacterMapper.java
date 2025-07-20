package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.combat.vo.Attributes;

final class CharacterMapper {

    CharacterMapper() {
    }

    static Character toDomain(CharacterEntity entity) {
        return Character.restore(
                new CharacterSnapshot(
                        new CharacterId(entity.getId()),
                        new AccountId(entity.getAccountId()),
                        new CharacterName(entity.getName()),
                        new Health(entity.getMaxHealth(), entity.getMaxHealth()),
                        new Attributes(
                                new Intelligence(entity.getIntelligence())
                        )
                )
        );
    }

    static CharacterEntity toEntity(Character character) {
        return new CharacterEntity(
                character.getSnapshot().getCharacterId().value(),
                character.getSnapshot().getAccountId().value(),
                character.getSnapshot().getCharacterName().value(),
                character.getSnapshot().getHealth().getMax(),
                character.getSnapshot().getAttributes().intelligence().getLevel()
        );
    }
}
