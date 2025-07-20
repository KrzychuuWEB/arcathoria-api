package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;

import java.util.UUID;

import static com.arcathoria.account.AccountDTOMapper.toAccountDTO;

public class AccountQueryFacade {

    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final CheckEmailExistsUseCase checkEmailExistsUseCase;

    AccountQueryFacade(final GetAccountByIdUseCase getAccountByIdUseCase,
                       final CheckEmailExistsUseCase checkEmailExistsUseCase) {
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.checkEmailExistsUseCase = checkEmailExistsUseCase;
    }

    public AccountDTO getById(final UUID accountId) {
        return toAccountDTO(getAccountByIdUseCase.execute(new AccountId(accountId)));
    }

    public void checkWhetherEmailIsExists(final String email) {
        checkEmailExistsUseCase.execute(new Email(email));
    }
}
