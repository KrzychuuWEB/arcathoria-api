package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterSnapshotTest {

    @Test
    void should_create_character_snapshot() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());

        CharacterSnapshot snapshot = CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build();

        assertThat(snapshot).isNotNull();
        assertThat(snapshot.getCharacterId()).isEqualTo(characterId);
        assertThat(snapshot.getName().value()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);
        assertThat(snapshot.getAccountId()).isEqualTo(accountId);
    }
}