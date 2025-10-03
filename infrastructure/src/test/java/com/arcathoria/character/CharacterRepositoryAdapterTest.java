package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.character.vo.AccountId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CharacterRepositoryAdapter.class)
class CharacterRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private CharacterRepositoryAdapter repository;

    @Test
    void should_save_new_character_with_valid_data() {
        AccountId account = new AccountId(UUID.randomUUID());

        Character character = Character.restore(CharacterSnapshotMother.create()
                .withAccountId(account.value())
                .build()
        );

        Character result = repository.save(character);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isNotNull();
        assertThat(result.getSnapshot().getAccountId().value()).isEqualTo(account.value());
        assertThat(result.getSnapshot().getCharacterName().value()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);
    }
}