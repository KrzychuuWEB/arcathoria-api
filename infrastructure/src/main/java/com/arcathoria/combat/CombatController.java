package com.arcathoria.combat;

import com.arcathoria.auth.AccountPrincipal;
import com.arcathoria.combat.dto.CombatIdDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ExecuteActionDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import com.arcathoria.combat.exception.CombatNotFoundException;
import com.arcathoria.combat.vo.CombatId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/combats")
@Tag(name = "Combats")
class CombatController {

    private final CombatFacade combatFacade;
    private final CombatQueryFacade combatQueryFacade;

    CombatController(final CombatFacade combatFacade, final CombatQueryFacade combatQueryFacade) {
        this.combatFacade = combatFacade;
        this.combatQueryFacade = combatQueryFacade;
    }

    @PostMapping("/init/pve")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "initPveCombat", summary = "Initial PVE combat")
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "COMBAT_NOT_FOUND", value = CombatOpenApiExamples.COMBAT_NOT_FOUND),
                            @ExampleObject(name = "MONSTER_NOT_FOUND", value = CombatOpenApiExamples.PARTICIPANT_MONSTER_NOT_FOUND),
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CombatOpenApiExamples.PARTICIPANT_CHARACTER_NOT_FOUND)
                    }
            )
    )
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "MONSTER_SERVICE", value = CombatOpenApiExamples.MONSTER_SERVICE_UNAVAILABLE),
                            @ExampleObject(name = "CHARACTER_SERVICE", value = CombatOpenApiExamples.CHARACTER_SERVICE_UNAVAILABLE)
                    }
            )
    )
    @ApiResponse(responseCode = "409",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CombatOpenApiExamples.ONLY_ONE_ACTIVE_COMBAT)
            )
    )
    CombatResultDTO initPveCombat(
            @Valid @RequestBody InitPveDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return combatFacade.initPVECombat(principal.id(), dto);
    }

    @PostMapping("/{id}/actions/execute")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "performActionInCombat", summary = "Perform action in combat")
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "COMBAT_NOT_FOUND", value = CombatOpenApiExamples.COMBAT_NOT_FOUND),
                            @ExampleObject(name = "PARTICIPANT_NOT_SELECTED", value = CombatOpenApiExamples.PARTICIPANT_CHARACTER_NOT_FOUND),
                            @ExampleObject(name = "ACTION_TYPE_NOT_FOUND", value = CombatOpenApiExamples.ACTION_TYPE_NOT_FOUND),
                    }
            )
    )
    @ApiResponse(responseCode = "409",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "COMBAT_ALREADY_FINISHED", value = CombatOpenApiExamples.COMBAT_ALREADY_FINISHED),
                            @ExampleObject(name = "COMBAT_WRONG_TURN", value = CombatOpenApiExamples.WRONG_TURN),
                    }
            )
    )
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = @ExampleObject(name = "COMBAT_ALREADY_FINISHED", value = CombatOpenApiExamples.CHARACTER_SERVICE_UNAVAILABLE)
            )
    )
    CombatResultDTO performActionInCombat(
            @PathVariable final UUID id,
            @Valid @RequestBody ExecuteActionDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        if (!id.equals(dto.combatId())) {
            throw new CombatNotFoundException(new CombatId(id));
        }

        return combatFacade.performActionInCombat(principal.id(), dto);
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getActiveCombatByParticipantId", summary = "Get active combat by participant id")
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CombatOpenApiExamples.PARTICIPANT_CHARACTER_NOT_FOUND),
                            @ExampleObject(name = "PARTICIPANT_NOT_HAS_ACTIVE_COMBAT", value = CombatOpenApiExamples.PARTICIPANT_NOT_HAS_ACTIVE_COMBAT)
                    }
            )
    )
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = @ExampleObject(name = "CHARACTER_SERVICE", value = CombatOpenApiExamples.CHARACTER_SERVICE_UNAVAILABLE)
            )
    )
    CombatIdDTO getActiveCombatByParticipantId(@AuthenticationPrincipal AccountPrincipal principal) {
        return combatQueryFacade.getActiveCombatForSelectedCharacterByAccountId(principal.id());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getCombat", summary = "Get combat by id")
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CombatOpenApiExamples.PARTICIPANT_NOT_FOUND_IN_COMBAT),
                            @ExampleObject(name = "COMBAT_NOT_FOUND", value = CombatOpenApiExamples.COMBAT_NOT_FOUND)
                    }
            )
    )
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CombatOpenApiProblemDetail.class),
                    examples = @ExampleObject(name = "CHARACTER_SERVICE", value = CombatOpenApiExamples.CHARACTER_SERVICE_UNAVAILABLE)
            )
    )
    CombatResultDTO getCombat(
            @PathVariable final UUID id,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return combatQueryFacade.getCombatByIdAndParticipantId(id, principal.id());
    }
}
