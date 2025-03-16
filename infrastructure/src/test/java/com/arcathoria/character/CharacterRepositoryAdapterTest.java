package com.arcathoria.character;

import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CharacterRepositoryAdapterTest extends PostgreSQLTestContainerConfig {

    @Autowired
    private CharacterRepositoryAdapter repository;

    @Autowired
    private AccountFacade accountFacade;

    @Test
    void should_save_new_character_with_valid_data() {
        AccountDTO account = accountFacade.createNewAccount(
                new RegisterDTO("example@email.com", "secretPassword123")
        );
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withAccountId(account.id())
                .build()
        );

        Character result = repository.save(character);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isNotNull();
        assertThat(result.getSnapshot().getAccountId().value()).isEqualTo(account.id());
        assertThat(result.getSnapshot().getCharacterName().value()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);
    }
}