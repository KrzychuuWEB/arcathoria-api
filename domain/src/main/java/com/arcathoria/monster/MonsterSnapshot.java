package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

record MonsterSnapshot(MonsterId monsterId, MonsterName monsterName, Health health) {
}
