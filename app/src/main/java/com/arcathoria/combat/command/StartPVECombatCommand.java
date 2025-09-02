package com.arcathoria.combat.command;

import com.arcathoria.character.dto.CharacterDTO;

public record StartPVECombatCommand(
        CharacterDTO attacker
) {
}
