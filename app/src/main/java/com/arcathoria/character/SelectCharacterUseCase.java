package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

class SelectCharacterUseCase {

    private final GetCharacterByIdUseCase getCharacterByIdUseCase;
    private final SelectCharacterCachePort selectCharacterCachePort;

    SelectCharacterUseCase(final GetCharacterByIdUseCase getCharacterByIdUseCase,
                           final SelectCharacterCachePort selectCharacterCachePort
    ) {
        this.getCharacterByIdUseCase = getCharacterByIdUseCase;
        this.selectCharacterCachePort = selectCharacterCachePort;
    }

    Character execute(final CharacterId characterId, final AccountId accountId) {
        Character character = getCharacterByIdUseCase.getOwned(characterId, accountId);

        selectCharacterCachePort.setValueAndSetExpiredTime(characterId, accountId);

        return character;
    }
}
