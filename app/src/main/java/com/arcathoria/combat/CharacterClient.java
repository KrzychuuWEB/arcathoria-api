package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.exception.CharacterNotFoundException;

import java.util.UUID;

interface CharacterClient {

    CharacterDTO getSelectedCharacterByAccountId(final UUID accountId) throws CharacterNotFoundException;
}
