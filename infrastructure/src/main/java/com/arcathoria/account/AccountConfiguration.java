package com.arcathoria.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountConfiguration {

    @Bean
    RegisterUseCase registerUseCase(
            final AccountRepository accountRepository,
            final PasswordEncoder passwordEncoder
    ) {
        return new EmailRegisterUseCase(accountRepository, passwordEncoder, new AccountFactory());
    }

    @Bean
    AccountFacade accountFacade(final RegisterUseCase registerUseCase) {
        return new AccountFacade(registerUseCase);
    }
}
