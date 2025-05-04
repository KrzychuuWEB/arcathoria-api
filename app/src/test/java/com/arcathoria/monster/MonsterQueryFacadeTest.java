package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonsterQueryFacadeTest {

    @Mock
    private GetMonsterByIdUseCase getMonsterByIdUseCase;

    @InjectMocks
    private MonsterQueryFacade monsterQueryFacade;

    @Test
    void should_return_monster_and_map_to_monsterDTO() {
        Monster monster = Monster.restore(MonsterSnapshotMother.aMonsterSnapshot().build());

        when(getMonsterByIdUseCase.execute(any(MonsterId.class))).thenReturn(monster);

        MonsterDTO result = monsterQueryFacade.getMonsterById(monster.getSnapshot().monsterId().value());

        assertThat(result).isInstanceOf(MonsterDTO.class);
        assertThat(result.id()).isEqualTo(monster.getSnapshot().monsterId().value());
        assertThat(result.name()).isEqualTo(monster.getSnapshot().monsterName().value());
        assertThat(result.currentHealth()).isEqualTo(monster.getSnapshot().health().getCurrent());
        assertThat(result.maxHealth()).isEqualTo(monster.getSnapshot().health().getMax());
    }
}