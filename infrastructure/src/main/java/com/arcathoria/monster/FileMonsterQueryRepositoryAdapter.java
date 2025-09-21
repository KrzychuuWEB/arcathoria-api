package com.arcathoria.monster;

import com.arcathoria.character.vo.Gauge;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.character.vo.Level;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.monster.dto.FileMonsterDTO;
import com.arcathoria.monster.exception.MonsterLoadingException;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class FileMonsterQueryRepositoryAdapter implements MonsterQueryRepository {

    private static final Logger log = LogManager.getLogger(FileMonsterQueryRepositoryAdapter.class);
    private final Map<UUID, FileMonsterDTO> monsterMap;

    FileMonsterQueryRepositoryAdapter(final ObjectMapper objectMapper) {
        try {
            InputStream is = new ClassPathResource("monsters.json").getInputStream();
            List<FileMonsterDTO> monsters = objectMapper.readValue(is, new TypeReference<>() {
            });
            this.monsterMap = monsters.stream()
                    .collect(Collectors.toMap(
                            FileMonsterDTO::monsterId,
                            Function.identity()
                    ));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new MonsterLoadingException("Failed to load monsters from file");
        }
    }

    @Override
    public Optional<Monster> getById(final MonsterId monsterId) {
        return Optional.ofNullable(monsterMap.get(monsterId.value()))
                .map(this::toDomain);
    }

    private Monster toDomain(final FileMonsterDTO dto) {
        return Monster.restore(new MonsterSnapshot(
                new MonsterId(dto.monsterId()),
                new MonsterName(dto.monsterName()),
                new Health(new Gauge(dto.currentHealth(), dto.maxHealth())),
                new Attributes(new Intelligence(new Level(dto.intelligence())))
        ));
    }
}
