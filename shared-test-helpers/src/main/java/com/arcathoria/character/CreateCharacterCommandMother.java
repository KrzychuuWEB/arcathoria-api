package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.command.CreateCharacterCommand;
import com.arcathoria.character.vo.CharacterName;

import java.util.UUID;

public final class CreateCharacterCommandMother {
    static final AccountId DEFAULT_ACCOUNT_ID = new AccountId(UUID.randomUUID());
    static final CharacterName DEFAULT_CHARACTER_NAME = new CharacterName("exampleCharacterName");

    private AccountId accountId = DEFAULT_ACCOUNT_ID;
    private CharacterName characterName = DEFAULT_CHARACTER_NAME;

    private CreateCharacterCommandMother() {
    }

    public static CreateCharacterCommandMother aCreateCharacterCommand() {
        return new CreateCharacterCommandMother();
    }

    public CreateCharacterCommandMother withAccountId(AccountId accountId) {
        this.accountId = accountId;
        return this;
    }

    public CreateCharacterCommandMother withCharacterName(CharacterName characterName) {
        this.characterName = characterName;
        return this;
    }

    public CreateCharacterCommandMother but() {
        return aCreateCharacterCommand().withAccountId(accountId).withCharacterName(characterName);
    }

    public CreateCharacterCommand build() {
        return new CreateCharacterCommand(accountId, characterName);
    }
}
