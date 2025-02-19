package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
class AccountController {

    private final AccountFacade accountFacade;

    AccountController(final AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping
    AccountDTO registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
        return accountFacade.createNewAccount(registerDTO);
    }
}
