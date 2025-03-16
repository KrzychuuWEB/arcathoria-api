package com.arcathoria.character;

import com.arcathoria.PostgreSQLTestContainerConfig;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.character.vo.CharacterName;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CharacterQueryRepositoryAdapterTest extends PostgreSQLTestContainerConfig {

    @Autowired
    private CharacterRepository repository;

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private CharacterQueryRepositoryAdapter queryRepository;

    @Test
    void should_return_true_if_character_name_exist() {
        AccountDTO account = accountFacade.createNewAccount(
                new RegisterDTO("example@email.com", "secretPassword123")
        );
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withAccountId(account.id())
                        .withCharacterName("existCharacterName")
                        .build()
        );
        repository.save(character);

        boolean result = queryRepository.existsByName(character.getSnapshot().getCharacterName());

        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_if_character_name_not_exist() {
        AccountDTO account = accountFacade.createNewAccount(
                new RegisterDTO("example@email.com", "secretPassword123")
        );
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withAccountId(account.id())
                        .withCharacterName("characterName")
                        .build()
        );
        repository.save(character);

        boolean result = queryRepository.existsByName(new CharacterName("character_name"));

        assertThat(result).isFalse();
    }
}