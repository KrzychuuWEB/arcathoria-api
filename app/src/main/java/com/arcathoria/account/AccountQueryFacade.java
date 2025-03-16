package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;

import java.util.UUID;

public class AccountQueryFacade {

    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final CheckEmailExistsUseCase checkEmailExistsUseCase;

    AccountQueryFacade(final GetAccountByIdUseCase getAccountByIdUseCase,
                       final CheckEmailExistsUseCase checkEmailExistsUseCase) {
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.checkEmailExistsUseCase = checkEmailExistsUseCase;
    }

    public AccountDTO getById(final UUID uuid) {
        return toDto(getAccountByIdUseCase.execute(new AccountId(uuid)));
    }

    public void checkWhetherEmailIsExists(final String email) {
        checkEmailExistsUseCase.execute(new Email(email));
    }

    private AccountDTO toDto(final Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(
                snapshot.getAccountId().value(),
                snapshot.getEmail().value()
        );
    }
}
