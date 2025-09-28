package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

import java.util.List;
import java.util.Optional;

interface CharacterQueryRepository {

    boolean existsByName(final CharacterName name);

    List<Character> getAllByAccountId(final AccountId accountId);

    Optional<Character> getById(final CharacterId characterId);
}
