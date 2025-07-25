package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CharacterOwnershipValidatorTest {

    private CharacterOwnershipValidator ownershipValidator;

    @BeforeEach
    void setUp() {
        this.ownershipValidator = new CharacterOwnershipValidator();
    }

    @Test
    void should_return_character_when_account_id_matches() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(
                CharacterSnapshotMother.create().withAccountId(accountId.value()).build()
        );

        Character result = ownershipValidator.validate(character, accountId);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId().value()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_ID);
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(accountId);
    }

    @Test
    void should_throw_AccessDeniedException_when_account_id_does_not_match() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(
                CharacterSnapshotMother.create().withAccountId(UUID.randomUUID()).build()
        );

        assertThatThrownBy(() -> ownershipValidator.validate(character, accountId)).isInstanceOf(AccessDeniedException.class);
    }
}