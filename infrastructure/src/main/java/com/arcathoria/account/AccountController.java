package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.auth.AccountPrincipal;
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

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts")
class AccountController {

    private final CreateAccountTransactionalAdapter createAccountTransactionalAdapter;
    private final AccountQueryFacade accountQueryFacade;

    AccountController(final CreateAccountTransactionalAdapter createAccountTransactionalAdapter,
                      final AccountQueryFacade accountQueryFacade) {
        this.createAccountTransactionalAdapter = createAccountTransactionalAdapter;
        this.accountQueryFacade = accountQueryFacade;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "register", summary = "Register new account")
    @ApiResponse(
            responseCode = "409",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = AccountOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = AccountOpenApiExamples.EMAIL_EXISTS)
            )
    )
    AccountDTO register(@Valid @RequestBody RegisterDTO registerDTO) {
        return createAccountTransactionalAdapter.transactionalCreateAccount(registerDTO);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "myAccount", summary = "Get current account")
    @ApiResponse(
            responseCode = "404",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = AccountOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = AccountOpenApiExamples.NOT_FOUND)
            )
    )
    AccountDTO myAccount(@AuthenticationPrincipal AccountPrincipal principal) {
        return accountQueryFacade.getById(principal.id());
    }
}
