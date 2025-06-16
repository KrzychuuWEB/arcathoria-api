package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

record MonsterSnapshot(MonsterId monsterId, MonsterName monsterName, Health health, Attributes attributes) {
}
