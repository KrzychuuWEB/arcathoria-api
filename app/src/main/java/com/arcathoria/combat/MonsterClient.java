package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.MonsterId;

interface MonsterClient {

    ParticipantView getMonsterById(final MonsterId monsterId);
}
