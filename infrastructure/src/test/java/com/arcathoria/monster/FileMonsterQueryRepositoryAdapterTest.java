package com.arcathoria.monster;

import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@ContextConfiguration(classes = {FileMonsterQueryRepositoryAdapter.class, JacksonAutoConfiguration.class})
class FileMonsterQueryRepositoryAdapterTest {

    @Autowired
    private FileMonsterQueryRepositoryAdapter adapter;

    @Test
    void should_return_monster_by_id_if_present() {
        UUID wolfUuid = UUID.fromString("bf4397d8-b4dc-361e-9b6d-191a352e9134");
        MonsterId monsterId = new MonsterId(wolfUuid);

        Optional<Monster> result = adapter.getById(monsterId);

        assertThat(result.get().getSnapshot().monsterId()).isEqualTo(monsterId);
        assertThat(result.get().getSnapshot().monsterName().value()).isEqualTo("Wilk");
    }
}