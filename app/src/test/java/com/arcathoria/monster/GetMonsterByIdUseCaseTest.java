package com.arcathoria.monster;

import com.arcathoria.monster.exception.MonsterNotFoundException;
import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMonsterByIdUseCaseTest {

    @Mock
    private MonsterQueryRepository monsterQueryRepository;

    @InjectMocks
    private GetMonsterByIdUseCase getMonsterByIdUseCase;

    @Test
    void should_return_monster_by_id_when_id_is_correct() {
        Monster monster = Monster.restore(MonsterSnapshotMother.aMonsterSnapshot().build());

        when(monsterQueryRepository.getById(any(MonsterId.class))).thenReturn(Optional.of(monster));

        Monster result = getMonsterByIdUseCase.execute(monster.getSnapshot().monsterId());

        assertThat(result.getSnapshot().monsterId()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_ID);
        assertThat(result.getSnapshot().monsterName()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_NAME);
        assertThat(result.getSnapshot().health()).isEqualTo(MonsterSnapshotMother.DEFAULT_HEALTH);
    }

    @Test
    void should_return_MonsterNotFoundException_when_id_is_not_correct() {
        when(monsterQueryRepository.getById(any(MonsterId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getMonsterByIdUseCase.execute(new MonsterId(null)))
                .isInstanceOf(MonsterNotFoundException.class)
                .message().contains("Monster with id");
    }
}