package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
class AccountController {

    private final CreateAccountTransactionalAdapter createAccountTransactionalAdapter;

    AccountController(final CreateAccountTransactionalAdapter createAccountTransactionalAdapter) {
        this.createAccountTransactionalAdapter = createAccountTransactionalAdapter;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(
            responseCode = "409",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(
                            value = "{\"detail\": \"Email already exists\", \"errorCode\": \"ERR_ACCOUNT_EMAIL_EXISTS\"}"
                    )
            )
    )
    AccountDTO registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
        return createAccountTransactionalAdapter.transactionalCreateAccount(registerDTO);
    }
}
