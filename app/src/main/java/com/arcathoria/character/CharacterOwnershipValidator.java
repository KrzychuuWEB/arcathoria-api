package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterAccessDenied;
import com.arcathoria.character.vo.AccountId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CharacterOwnershipValidator {


    private static final Logger log = LogManager.getLogger(CharacterOwnershipValidator.class);

    Character validate(final Character character, final AccountId accountId) {
        if (!character.getSnapshot().getAccountId().equals(accountId)) {
            log.warn("The character {} is not assigned to the account {}",
                    character.getSnapshot().getCharacterId().value(),
                    accountId.value()
            );
            throw new CharacterAccessDenied();
        }

        return character;
    }
}
