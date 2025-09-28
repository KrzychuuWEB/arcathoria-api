package com.arcathoria.character;

import com.arcathoria.character.vo.AccountId;

class RemoveSelectedCharacterUseCase {

    private final SelectCharacterCachePort selectCharacterCachePort;
    private final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase;

    RemoveSelectedCharacterUseCase(
            final SelectCharacterCachePort selectCharacterCachePort,
            final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase
    ) {
        this.selectCharacterCachePort = selectCharacterCachePort;
        this.getSelectedCharacterFromCacheUseCase = getSelectedCharacterFromCacheUseCase;
    }

    void execute(final AccountId accountId) {
        getSelectedCharacterFromCacheUseCase.execute(accountId);

        selectCharacterCachePort.remove(accountId);
    }
}
