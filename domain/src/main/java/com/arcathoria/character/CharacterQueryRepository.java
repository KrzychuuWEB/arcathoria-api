package com.arcathoria.character;

import com.arcathoria.character.vo.CharacterName;

interface CharacterQueryRepository {

    boolean existsByName(CharacterName name);
}
