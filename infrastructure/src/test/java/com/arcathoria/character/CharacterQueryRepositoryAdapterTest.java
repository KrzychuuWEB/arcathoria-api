package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.AccountFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CharacterQueryRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private CharacterRepository repository;

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private CharacterQueryRepositoryAdapter queryRepository;

    @Test
    void should_return_true_if_character_name_exist() {
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withAccountId(getAccount().id())
                        .withCharacterName("existCharacterName")
                        .build()
        );
        repository.save(character);

        boolean result = queryRepository.existsByName(character.getSnapshot().getCharacterName());

        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_if_character_name_not_exist() {
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withAccountId(getAccount().id())
                        .withCharacterName("characterName")
                        .build()
        );
        repository.save(character);

        boolean result = queryRepository.existsByName(new CharacterName("character_name"));

        assertThat(result).isFalse();
    }

    @Test
    void should_return_all_characters_by_account_id() {
        UUID accountId = getAccount().id();
        UUID otherAccountId = getAccount().id();

        List<Character> characters = getCharactersList(accountId);

        repository.save(Character.restore(CharacterSnapshotMother.create()
                .withAccountId(otherAccountId)
                .withCharacterName("characterName")
                .build()
        ));

        List<Character> result = queryRepository.getAllByAccountId(new AccountId(accountId));

        assertThat(result).hasSameSizeAs(characters);
        assertThat(sortedCharacterAndGetByIndex(result, 0).getCharacterId())
                .isEqualTo(sortedCharacterAndGetByIndex(characters, 0).getCharacterId());
        assertThat(sortedCharacterAndGetByIndex(result, 0).getCharacterName())
                .isEqualTo(sortedCharacterAndGetByIndex(characters, 0).getCharacterName());
        assertThat(sortedCharacterAndGetByIndex(result, 1).getCharacterId())
                .isEqualTo(sortedCharacterAndGetByIndex(characters, 1).getCharacterId());
        assertThat(sortedCharacterAndGetByIndex(result, 1).getCharacterName())
                .isEqualTo(sortedCharacterAndGetByIndex(characters, 1).getCharacterName());
    }

    @Test
    void should_get_character_by_id() {
        Character character = repository.save(Character.restore(CharacterSnapshotMother.create()
                .withAccountId(UUID.randomUUID())
                .withCharacterName("characterName")
                .build()
        ));

        Optional<Character> result = queryRepository.getById(character.getSnapshot().getCharacterId());

        assertThat(result).isNotEmpty();
        assertThat(result.get().getSnapshot().getCharacterName()).isEqualTo(character.getSnapshot().getCharacterName());
        assertThat(result.get().getSnapshot().getCharacterId()).isEqualTo(character.getSnapshot().getCharacterId());
    }

    @Test
    void should_return_empty_optional_if_character_id_is_not_exists() {
        Optional<Character> result = queryRepository.getById(new CharacterId(UUID.randomUUID()));

        assertThat(result).isEmpty();
    }

    private CharacterSnapshot sortedCharacterAndGetByIndex(List<Character> characters, Integer index) {
        List<Character> list = characters.stream()
                .sorted(Comparator.comparing(character -> character.getSnapshot().getCharacterName().value()))
                .toList();

        return list.get(index).getSnapshot();
    }

    private List<Character> getCharactersList(UUID accountId) {
        return List.of(
                repository.save(Character.restore(CharacterSnapshotMother.create()
                        .withAccountId(accountId)
                        .withCharacterName("characterName")
                        .build()
                )),
                repository.save(Character.restore(CharacterSnapshotMother.create()
                        .withAccountId(accountId)
                        .withCharacterName("characterName")
                        .build()
                ))
        );
    }

    private AccountDTO getAccount() {
        return accountFacade.createNewAccount(
                new RegisterDTO("test_" + UUID.randomUUID() + "@email.com", "secretPassword123")
        );
    }
}