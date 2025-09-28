package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;

import java.util.List;

class GetAllCharactersByAccountIdUseCase {

    private final CharacterQueryRepository characterQueryRepository;
    private final AccountClient accountClient;

    GetAllCharactersByAccountIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final AccountClient accountClient
    ) {
        this.characterQueryRepository = characterQueryRepository;
        this.accountClient = accountClient;
    }

    List<Character> execute(final AccountId accountId) {
        accountClient.getById(accountId);

        return characterQueryRepository.getAllByAccountId(accountId);
    }
}
