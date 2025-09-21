package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;

import java.util.Optional;
import java.util.UUID;

interface CharacterClient {

    Optional<CharacterDTO> getSelectedCharacterByAccountId(final UUID accountId);
}
