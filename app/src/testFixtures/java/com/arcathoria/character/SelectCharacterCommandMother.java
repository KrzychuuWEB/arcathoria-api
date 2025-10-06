package com.arcathoria.character;

import com.arcathoria.character.command.SelectCharacterCommand;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;

import java.util.UUID;

final class SelectCharacterCommandMother {
    static final AccountId DEFAULT_ACCOUNT_ID = new AccountId(UUID.randomUUID());
    static final CharacterId DEFAULT_CHARACTER_ID = new CharacterId(UUID.randomUUID());

    private AccountId accountId = DEFAULT_ACCOUNT_ID;
    private CharacterId characterId = DEFAULT_CHARACTER_ID;

    private SelectCharacterCommandMother() {
    }

    static SelectCharacterCommandMother aSelectCharacterCommand() {
        return new SelectCharacterCommandMother();
    }

    SelectCharacterCommandMother withAccountId(AccountId accountId) {
        this.accountId = accountId;
        return this;
    }

    SelectCharacterCommandMother withCharacterId(CharacterId characterId) {
        this.characterId = characterId;
        return this;
    }

    SelectCharacterCommand build() {
        return new SelectCharacterCommand(accountId, characterId);
    }
}
