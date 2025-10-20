package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.exception.CharacterNotOwnedException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCharacterByIdUseCaseTest {

    @Mock
    private CharacterQueryRepository repository;

    private final CharacterOwnershipValidator ownershipValidator = new CharacterOwnershipValidator();

    private GetCharacterByIdUseCase getCharacterByIdUseCase;

    @BeforeEach
    void setUp() {
        this.getCharacterByIdUseCase = new GetCharacterByIdUseCase(repository, ownershipValidator);
    }

    @Test
    void should_return_character_when_character_exists() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(repository.getById(any(CharacterId.class))).thenReturn(Optional.of(character));

        Character result = getCharacterByIdUseCase.get(characterId);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(accountId);

        verify(repository).getById(any(CharacterId.class));
    }

    @Test
    void should_throw_CharacterNotFoundException_when_character_does_not_exist() {
        when(repository.getById(any(CharacterId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getCharacterByIdUseCase.get(new CharacterId(UUID.randomUUID()))).isInstanceOf(CharacterNotFoundException.class);

        verify(repository).getById(any(CharacterId.class));
    }

    @Test
    void should_return_character_when_character_is_owned_by_account() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(repository.getById(any(CharacterId.class))).thenReturn(Optional.of(character));

        Character result = getCharacterByIdUseCase.getOwned(characterId, accountId);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(character.getSnapshot().getCharacterId());
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(character.getSnapshot().getAccountId());

        verify(repository).getById(any(CharacterId.class));
    }

    @Test
    void should_throw_AccessDeniedException_when_character_is_not_owned_by_account() {
        AccountId badAccountId = new AccountId(UUID.randomUUID());
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(repository.getById(any(CharacterId.class))).thenReturn(Optional.of(character));

        assertThatThrownBy(() -> getCharacterByIdUseCase.getOwned(characterId, badAccountId)).isInstanceOf(CharacterNotOwnedException.class);

        verify(repository).getById(any(CharacterId.class));
    }
}