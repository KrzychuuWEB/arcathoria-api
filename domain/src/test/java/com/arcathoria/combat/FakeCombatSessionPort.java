package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class FakeCombatSessionPort implements CombatSessionStore {

    private final Set<CombatSnapshot> combats = new HashSet<>();

    @Override
    public CombatSnapshot save(final CombatSnapshot snapshot) {
        combats.removeIf(c -> c.combatId().equals(snapshot.combatId()));
        combats.add(snapshot);
        return snapshot;
    }

    @Override
    public Optional<CombatSnapshot> getCombatById(final CombatId combatId) {
        return combats.stream()
                .filter(c -> c.combatId().equals(combatId))
                .findFirst();
    }

    @Override
    public Optional<CombatId> getActiveCombatIdByParticipantId(final ParticipantId participantId) {
        return combats.stream()
                .filter(c -> c.combatStatus() == CombatStatus.IN_PROGRESS)
                .filter(c -> c.attacker().participantId().equals(participantId)
                        || c.defender().participantId().equals(participantId))
                .map(CombatSnapshot::combatId)
                .findFirst();
    }

    @Override
    public void remove(final CombatId combatId) {
        combats.removeIf(c -> c.combatId().equals(combatId));
    }
}
