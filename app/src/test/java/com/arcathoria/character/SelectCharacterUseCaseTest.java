package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.exception.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectCharacterUseCaseTest {

    @Mock
    private GetCharacterByIdUseCase getCharacterByIdUseCase;

    @Mock
    private SelectCharacterCachePort selectCharacterCachePort;

    @InjectMocks
    private SelectCharacterUseCase selectCharacterUseCase;

    @Test
    void should_return_character_when_account_owned_character() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenReturn(character);

        Character result = selectCharacterUseCase.execute(characterId, accountId);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(accountId);

        verify(getCharacterByIdUseCase).getOwned(any(CharacterId.class), any(AccountId.class));
        verify(selectCharacterCachePort).setValueAndSetExpiredTime(any(CharacterId.class), any(AccountId.class));
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_not_found() {
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> selectCharacterUseCase.execute(
                new CharacterId(UUID.randomUUID()),
                new AccountId(UUID.randomUUID())
        )).isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    void should_return_AccessDeniedException_when_account_not_owned_character() {
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(AccessDeniedException.class);

        assertThatThrownBy(() -> selectCharacterUseCase.execute(
                new CharacterId(UUID.randomUUID()),
                new AccountId(UUID.randomUUID())
        )).isInstanceOf(AccessDeniedException.class);
    }
}