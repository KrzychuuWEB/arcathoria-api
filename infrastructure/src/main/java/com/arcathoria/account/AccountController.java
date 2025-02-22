package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
class AccountController {

    private final AccountFacade accountFacade;

    AccountController(final AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AccountDTO registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
        return accountFacade.createNewAccount(registerDTO);
    }
}
