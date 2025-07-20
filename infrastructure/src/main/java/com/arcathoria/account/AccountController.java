package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    AccountDTO registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
        return createAccountTransactionalAdapter.transactionalCreateAccount(registerDTO);
    }
}
