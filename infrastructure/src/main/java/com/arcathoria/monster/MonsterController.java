package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/monsters")
class MonsterController {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterController(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(
            responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = MonsterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = MonsterOpenApiExamples.MONSTER_NOT_FOUND)
            )
    )
    MonsterDTO getMonsterById(final @PathVariable UUID id) {
        return monsterQueryFacade.getMonsterById(id);
    }
}

