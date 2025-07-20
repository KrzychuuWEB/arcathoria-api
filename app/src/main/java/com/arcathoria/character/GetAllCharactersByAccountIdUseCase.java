package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.vo.AccountId;

import java.util.List;

class GetAllCharactersByAccountIdUseCase {

    private final CharacterQueryRepository characterQueryRepository;
    private final AccountQueryFacade accountQueryFacade;

    GetAllCharactersByAccountIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final AccountQueryFacade accountQueryFacade
    ) {
        this.characterQueryRepository = characterQueryRepository;
        this.accountQueryFacade = accountQueryFacade;
    }

    List<Character> execute(final AccountId accountId) {
        accountQueryFacade.getById(accountId.value());

        return characterQueryRepository.getAllByAccountId(accountId);
    }
}
