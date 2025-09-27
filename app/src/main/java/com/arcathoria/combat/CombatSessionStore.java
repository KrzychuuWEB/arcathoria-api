package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.Optional;

interface CombatSessionStore {

    CombatSnapshot save(final CombatSnapshot snapshot);

    Optional<CombatSnapshot> getCombatById(final CombatId combatId);

    Optional<CombatId> getActiveCombatIdByParticipantId(final ParticipantId participantId);

    void remove(final CombatId combatId);
}
