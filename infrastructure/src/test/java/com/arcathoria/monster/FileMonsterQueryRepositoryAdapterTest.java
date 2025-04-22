package com.arcathoria.monster;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileMonsterQueryRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private FileMonsterQueryRepositoryAdapter adapter;

    @Test
    void should_return_monster_by_id_if_present() {
        MonsterId monsterId = new MonsterId("wolf");

        Optional<Monster> result = adapter.getById(monsterId);

        assertThat(result.get().getSnapshot().monsterId()).isEqualTo(monsterId);
        assertThat(result.get().getSnapshot().monsterName().value()).isEqualTo("Wilk");
    }
}