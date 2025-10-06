package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

interface SelectCharacterCachePort {

    void setValueAndSetExpiredTime(final CharacterId characterId, final AccountId accountId);

    CharacterId getAndSetNewExpiredTime(final AccountId accountId);

    void remove(final AccountId accountId);
}
