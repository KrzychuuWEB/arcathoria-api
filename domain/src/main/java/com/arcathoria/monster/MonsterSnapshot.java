package com.arcathoria.monster;

import com.arcathoria.monster.vo.Attributes;
import com.arcathoria.monster.vo.Health;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

record MonsterSnapshot(MonsterId monsterId, MonsterName monsterName, Health health, Attributes attributes) {
}
