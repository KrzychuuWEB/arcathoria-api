package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.*;
import com.arcathoria.character.vo.CharacterName;
import com.arcathoria.combat.vo.Attributes;

class CharacterFactory {

    Character createCharacter(AccountId accountId, CharacterName name) {
        return Character.restore(
                new CharacterSnapshot(
                        new CharacterId(null),
                        accountId,
                        name,
                        new Health(100.0, 100.0),
                        new Attributes(
                                new Intelligence(new Level(1))
                        )
                )
        );
    }
}
