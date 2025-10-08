package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

class GetSelectedCharacterFromCacheUseCase {

    private final GetCharacterByIdUseCase getCharacterByIdUseCase;
    private final SelectCharacterCachePort selectCharacterCachePort;

    GetSelectedCharacterFromCacheUseCase(final GetCharacterByIdUseCase getCharacterByIdUseCase,
                                         final SelectCharacterCachePort selectCharacterCachePort
    ) {
        this.getCharacterByIdUseCase = getCharacterByIdUseCase;
        this.selectCharacterCachePort = selectCharacterCachePort;
    }

    Character execute(final AccountId accountId) {
        CharacterId characterId = selectCharacterCachePort.getAndSetNewExpiredTime(accountId);

        return getCharacterByIdUseCase.getOwned(characterId, accountId);
    }
}
