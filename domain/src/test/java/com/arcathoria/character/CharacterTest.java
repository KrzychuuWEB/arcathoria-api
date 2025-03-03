package com.arcathoria.character;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterTest {

    @Test
    void should_restore_character_from_snapshot() {
        CharacterSnapshot snapshot = CharacterSnapshotMother.create()
                .withCharacterId(UUID.randomUUID())
                .withAccountId(UUID.randomUUID())
                .build();

        Character result = Character.restore(snapshot);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(snapshot.getCharacterId());
        assertThat(result.getSnapshot().getName().getValue()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(snapshot.getAccountId());
    }

    @Test
    void should_get_snapshot_from_character() {
        CharacterSnapshot snapshot = CharacterSnapshotMother.create()
                .withCharacterId(UUID.randomUUID())
                .withAccountId(UUID.randomUUID())
                .build();
        Character character = Character.restore(snapshot);

        CharacterSnapshot result = character.getSnapshot();

        assertThat(result.getCharacterId()).isEqualTo(snapshot.getCharacterId());
        assertThat(result.getName().getValue()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);
        assertThat(result.getAccountId()).isEqualTo(snapshot.getAccountId());
    }
}