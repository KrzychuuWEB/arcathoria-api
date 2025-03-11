package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

import java.util.UUID;

public class CharacterSnapshotMother {

    static final UUID DEFAULT_CHARACTER_ID = null;
    static final String DEFAULT_CHARACTER_NAME = "Example_Character-Name";
    static final UUID DEFAULT_ACCOUNT_ID = null;

    private CharacterId characterId = new CharacterId(DEFAULT_CHARACTER_ID);
    private CharacterName characterName = new CharacterName(DEFAULT_CHARACTER_NAME);
    private AccountId accountId = new AccountId(DEFAULT_ACCOUNT_ID);

    static CharacterSnapshotMother create() {
        return new CharacterSnapshotMother();
    }

    CharacterSnapshotMother withCharacterId(UUID uuid) {
        this.characterId = new CharacterId(uuid);
        return this;
    }

    CharacterSnapshotMother withCharacterName(String name) {
        this.characterName = new CharacterName(name);
        return this;
    }

    CharacterSnapshotMother withAccountId(UUID uuid) {
        this.accountId = new AccountId(uuid);
        return this;
    }

    CharacterSnapshot build() {
        return new CharacterSnapshot(characterId, accountId, characterName);
    }
}
