package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monsters")
class MonsterController {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterController(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    MonsterDTO getMonsterById(final @PathVariable String id) {
        return monsterQueryFacade.getMonsterById(id);
    }
}
