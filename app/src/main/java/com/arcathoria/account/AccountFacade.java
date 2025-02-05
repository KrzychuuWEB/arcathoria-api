package com.arcathoria.account;

public class AccountFacade {

    AccountFacade(final RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    private final RegisterUseCase registerUseCase;
    
}
