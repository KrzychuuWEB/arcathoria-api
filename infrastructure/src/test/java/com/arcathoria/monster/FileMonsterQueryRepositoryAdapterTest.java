package com.arcathoria.monster;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FileMonsterQueryRepositoryAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private FileMonsterQueryRepositoryAdapter adapter;

    @Test
    void should_return_monster_by_id_if_present() {
        MonsterId monsterId = new MonsterId("wolf");

        Monster result = adapter.getById(monsterId);

        assertThat(result.getSnapshot().monsterId()).isEqualTo(monsterId);
        assertThat(result.getSnapshot().monsterName().value()).isEqualTo("Wilk");
    }

    @Test
    void should_throw_exception_when_monster_by_id_not_found() {
        assertThatThrownBy(() -> adapter.getById(new MonsterId("not_found_monster"))).isInstanceOf(MonsterNotFoundException.class);
    }
}