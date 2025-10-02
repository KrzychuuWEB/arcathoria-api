package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterAccessDenied;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.BeforeEach;
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
class GetSelectedCharacterFromCacheUseCaseTest {

    @Mock
    private GetCharacterByIdUseCase getCharacterByIdUseCase;

    @Mock
    private SelectCharacterCachePort selectCharacterCachePort;

    @InjectMocks
    private GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase;

    private AccountId accountId;
    private CharacterId characterId;

    @BeforeEach
    void setValueAndSetExpiredTimeUp() {
        accountId = new AccountId(UUID.randomUUID());
        characterId = new CharacterId(UUID.randomUUID());
    }

    @Test
    void should_return_character_when_get_character_id_from_cache_and_check_owned_character() {
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(selectCharacterCachePort.getAndSetNewExpiredTime(any(AccountId.class))).thenReturn(characterId);
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenReturn(character);

        Character result = getSelectedCharacterFromCacheUseCase.execute(accountId);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(characterId);
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(accountId);

        verify(selectCharacterCachePort).getAndSetNewExpiredTime(accountId);
        verify(getCharacterByIdUseCase).getOwned(characterId, accountId);
    }

    @Test
    void should_return_AccessDeniedException_when_character_not_owned_for_account() {
        when(selectCharacterCachePort.getAndSetNewExpiredTime(any(AccountId.class))).thenReturn(characterId);
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(CharacterAccessDenied.class);

        assertThatThrownBy(() -> getSelectedCharacterFromCacheUseCase.execute(accountId)).isInstanceOf(CharacterAccessDenied.class);

        verify(selectCharacterCachePort).getAndSetNewExpiredTime(accountId);
        verify(getCharacterByIdUseCase).getOwned(characterId, accountId);
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_not_found() {
        when(selectCharacterCachePort.getAndSetNewExpiredTime(any(AccountId.class))).thenReturn(characterId);
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> getSelectedCharacterFromCacheUseCase.execute(accountId)).isInstanceOf(CharacterNotFoundException.class);

        verify(selectCharacterCachePort).getAndSetNewExpiredTime(accountId);
        verify(getCharacterByIdUseCase).getOwned(characterId, accountId);
    }
}