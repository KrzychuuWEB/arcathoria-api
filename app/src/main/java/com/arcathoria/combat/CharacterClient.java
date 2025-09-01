package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;

import java.util.UUID;

interface CharacterClient {

    CharacterDTO getSelectedCharacterByAccountId(final UUID accountId);
}
