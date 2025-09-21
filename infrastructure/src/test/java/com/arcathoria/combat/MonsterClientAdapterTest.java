package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.monster.dto.MonsterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonsterClientAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private MonsterClientAdapter monsterClientAdapter;

    @Test
    void should_return_a_monster_by_id() {
        UUID monsterId = UUID.fromString("bf4397d8-b4dc-361e-9b6d-191a352e9134");

        Optional<MonsterDTO> monsterDTO = monsterClientAdapter.getMonsterById(monsterId);
        MonsterDTO result = monsterDTO.get();

        assertThat(result.id()).isEqualTo(monsterId);
    }

    @Test
    void should_return_empty_optional_when_monster_not_found() {
        UUID monsterId = UUID.randomUUID();
        Optional<MonsterDTO> result = monsterClientAdapter.getMonsterById(monsterId);

        assertThat(result).isEmpty();
    }
}