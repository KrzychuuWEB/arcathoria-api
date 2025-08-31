package com.arcathoria.combat;

import com.arcathoria.combat.dto.CombatResultDTO;

final class CombatMapper {

    static CombatResultDTO toCombatResultDTO(final Combat combat) {
        return new CombatResultDTO(
                combat.getSnapshot().combatId().value()
        );
    }
}
