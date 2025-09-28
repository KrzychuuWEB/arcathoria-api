package com.arcathoria.character;

import com.arcathoria.Gauge;
import com.arcathoria.Level;
import com.arcathoria.character.vo.*;
import com.arcathoria.character.vo.CharacterName;

import java.util.UUID;

class CharacterFactory {

    Character createCharacter(final AccountId accountId, final CharacterName name) {
        return Character.restore(
                new CharacterSnapshot(
                        new CharacterId(UUID.randomUUID()),
                        accountId,
                        name,
                        new Health(new Gauge(100, 100)),
                        new Attributes(
                                new Intelligence(new Level(1))
                        )
                )
        );
    }
}
