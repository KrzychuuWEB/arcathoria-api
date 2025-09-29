package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonsterClientAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private MonsterClientAdapter monsterClientAdapter;

    @Test
    void should_return_a_monster_by_id() {
        MonsterId monsterId = new MonsterId(UUID.fromString("bf4397d8-b4dc-361e-9b6d-191a352e9134"));

        ParticipantView result = monsterClientAdapter.getMonsterById(monsterId);

        assertThat(result.id()).isEqualTo(monsterId.value());
    }
}