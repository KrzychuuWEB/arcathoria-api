package com.arcathoria.character;

import com.arcathoria.character.command.CreateCharacterCommand;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterName;

import java.util.UUID;

final class CreateCharacterCommandMother {
    static final AccountId DEFAULT_ACCOUNT_ID = new AccountId(UUID.randomUUID());
    static final CharacterName DEFAULT_CHARACTER_NAME = new CharacterName("exampleCharacterName");

    private AccountId accountId = DEFAULT_ACCOUNT_ID;
    private CharacterName characterName = DEFAULT_CHARACTER_NAME;

    private CreateCharacterCommandMother() {
    }

    static CreateCharacterCommandMother aCreateCharacterCommand() {
        return new CreateCharacterCommandMother();
    }

    CreateCharacterCommandMother withAccountId(AccountId accountId) {
        this.accountId = accountId;
        return this;
    }

    CreateCharacterCommandMother withCharacterName(CharacterName characterName) {
        this.characterName = characterName;
        return this;
    }

    CreateCharacterCommandMother but() {
        return aCreateCharacterCommand().withAccountId(accountId).withCharacterName(characterName);
    }

    CreateCharacterCommand build() {
        return new CreateCharacterCommand(accountId, characterName);
    }
}
