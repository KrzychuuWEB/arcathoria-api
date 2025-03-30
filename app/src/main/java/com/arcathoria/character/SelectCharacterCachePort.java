package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

interface SelectCharacterCachePort {

    void set(final CharacterId characterId, final AccountId accountId);

    CharacterId get(final AccountId accountId);

    void remove(final AccountId accountId);
}
