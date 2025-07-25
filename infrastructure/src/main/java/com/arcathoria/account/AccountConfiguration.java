package com.arcathoria.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountConfiguration {

    @Bean
    RegisterUseCase registerUseCase(
            final AccountRepository accountRepository,
            final AccountQueryFacade accountQueryFacade,
            final PasswordEncoder passwordEncoder
    ) {
        return new EmailRegisterUseCase(accountRepository, accountQueryFacade, passwordEncoder, new AccountFactory());
    }

    @Bean
    GetAccountByIdUseCase getAccountByIdUseCase(final AccountQueryRepository accountQueryRepository) {
        return new GetAccountByIdUseCase(accountQueryRepository);
    }

    @Bean
    CheckEmailExistsUseCase checkEmailExistsUseCase(final AccountQueryRepository accountQueryRepository) {
        return new CheckEmailExistsUseCase(accountQueryRepository);
    }

    @Bean
    AccountQueryFacade accountQueryFacade(
            final GetAccountByIdUseCase getAccountByIdUseCase,
            final CheckEmailExistsUseCase checkEmailExistsUseCase
    ) {
        return new AccountQueryFacade(getAccountByIdUseCase, checkEmailExistsUseCase);
    }

    @Bean
    AccountFacade accountFacade(final RegisterUseCase registerUseCase) {
        return new AccountFacade(registerUseCase);
    }
}
