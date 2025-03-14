package com.arcathoria.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountConfiguration {

    @Bean
    RegisterUseCase registerUseCase(
            final AccountRepository accountRepository,
            final AccountQueryRepository accountQueryRepository,
            final PasswordEncoder passwordEncoder
    ) {
        return new EmailRegisterUseCase(accountRepository, accountQueryRepository, passwordEncoder, new AccountFactory());
    }

    @Bean
    GetAccountByIdUseCase getAccountByIdUseCase(final AccountQueryRepository accountQueryRepository) {
        return new GetAccountByIdUseCase(accountQueryRepository);
    }

    @Bean
    AccountQueryFacade accountQueryFacade(final GetAccountByIdUseCase getAccountByIdUseCase) {
        return new AccountQueryFacade(getAccountByIdUseCase);
    }

    @Bean
    AccountFacade accountFacade(final RegisterUseCase registerUseCase) {
        return new AccountFacade(registerUseCase);
    }
}
