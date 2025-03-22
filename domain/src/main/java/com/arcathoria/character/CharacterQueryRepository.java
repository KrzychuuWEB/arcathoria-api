package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterName;

import java.util.List;

interface CharacterQueryRepository {

    boolean existsByName(final CharacterName name);

    List<Character> getAllByAccountId(final AccountId accountId);
}
