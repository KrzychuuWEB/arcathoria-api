package com.arcathoria.combat.command;

import com.arcathoria.combat.vo.Participant;

public record StartPVECombatCommand(
        Participant attacker,
        Participant defender
) {
}
