package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.AccountId;

interface CharacterClient {

    ParticipantView getSelectedCharacterByAccountId(final AccountId accountId);
}
