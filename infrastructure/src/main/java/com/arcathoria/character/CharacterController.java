package com.arcathoria.character;

import com.arcathoria.auth.AccountPrincipal;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
class CharacterController {

    private final CharacterFacade characterFacade;
    private final CharacterQueryFacade characterQueryFacade;
    private final CreateCharacterTransactionalAdapter createCharacterTransactionalAdapter;

    CharacterController(
            final CharacterFacade characterFacade,
            final CharacterQueryFacade characterQueryFacade,
            final CreateCharacterTransactionalAdapter createCharacterTransactionalAdapter
    ) {
        this.characterFacade = characterFacade;
        this.characterQueryFacade = characterQueryFacade;
        this.createCharacterTransactionalAdapter = createCharacterTransactionalAdapter;
    }

    @GetMapping("/selects")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.ACCOUNT_SERVICE_UNAVAILABLE)
            )
    )
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_OWNER_NOT_FOUND)
            )
    )
    List<CharacterDTO> getAllByAccountId(@AuthenticationPrincipal AccountPrincipal principal) {
        return characterQueryFacade.getAllByAccountId(principal.id());
    }

    @PostMapping("/selects")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CharacterOpenApiExamples.CHARACTER_NOT_FOUND),
                            @ExampleObject(name = "CHARACTER_NOT_SELECTED", value = CharacterOpenApiExamples.CHARACTER_NOT_SELECTED)
                    }
            )
    )
    @ApiResponse(responseCode = "403",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_NOT_OWNED)
            )
    )
    CharacterDTO selectCharacter(
            @Valid @RequestBody SelectCharacterDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return characterFacade.selectCharacter(dto, principal.id());
    }

    @GetMapping("/selects/me")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CharacterOpenApiExamples.CHARACTER_NOT_FOUND),
                            @ExampleObject(name = "CHARACTER_NOT_SELECTED", value = CharacterOpenApiExamples.CHARACTER_NOT_SELECTED)
                    }
            )
    )
    @ApiResponse(responseCode = "403",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_NOT_OWNED)
            )
    )
    CharacterDTO getSelectedCharacter(@AuthenticationPrincipal AccountPrincipal principal) {
        return characterQueryFacade.getSelectedCharacter(principal.id());
    }

    @DeleteMapping("/selects")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(name = "CHARACTER_NOT_FOUND", value = CharacterOpenApiExamples.CHARACTER_NOT_FOUND),
                            @ExampleObject(name = "CHARACTER_NOT_SELECTED", value = CharacterOpenApiExamples.CHARACTER_NOT_SELECTED)
                    }
            )
    )
    @ApiResponse(responseCode = "403",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_NOT_OWNED)
            )
    )
    void removeSelectedCharacter(@AuthenticationPrincipal AccountPrincipal principal) {
        characterFacade.removeSelectedCharacter(principal.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "409",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_NAME_EXISTS)
            )
    )
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.ACCOUNT_SERVICE_UNAVAILABLE)
            )
    )
    @ApiResponse(responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = CharacterOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = CharacterOpenApiExamples.CHARACTER_OWNER_NOT_FOUND)
            )
    )
    CharacterDTO create(
            @Valid @RequestBody CreateCharacterDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return createCharacterTransactionalAdapter.transactionalCreateCharacter(dto, principal.id());
    }
}
