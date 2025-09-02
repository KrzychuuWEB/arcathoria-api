package com.arcathoria.combat.command;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.monster.dto.MonsterDTO;

public record StartPVECombatCommand(
        CharacterDTO attacker,
        MonsterDTO defender
) {
}
