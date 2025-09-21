package com.arcathoria.combat;

import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.ParticipantId;

record ParticipantSnapshot(
        ParticipantId participantId,
        Health health,
        Attributes attributes
) {
}
