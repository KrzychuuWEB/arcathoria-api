package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.*;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.combat.vo.Attributes;

final class CharacterMapper {

    CharacterMapper() {
    }

    static Character toDomain(final CharacterEntity entity) {
        return Character.restore(
                new CharacterSnapshot(
                        new CharacterId(entity.getId()),
                        new AccountId(entity.getAccountId()),
                        new CharacterName(entity.getName()),
                        new Health(new Gauge(entity.getMaxHealth(), entity.getMaxHealth())),
                        new Attributes(
                                new Intelligence(new Level(entity.getIntelligence()))
                        )
                )
        );
    }

    static CharacterEntity toEntity(final Character character) {
        return new CharacterEntity(
                character.getSnapshot().getCharacterId().value(),
                character.getSnapshot().getAccountId().value(),
                character.getSnapshot().getCharacterName().value(),
                character.getSnapshot().getHealth().getMax(),
                character.getSnapshot().getAttributes().intelligence().level().value()
        );
    }
}
