package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.exception.AccessDeniedException;

class CharacterOwnershipValidator {

    Character validate(final Character character, final AccountId accountId) {
        if (!character.getSnapshot().getAccountId().equals(accountId)) {
            throw new AccessDeniedException();
        }

        return character;
    }
}
