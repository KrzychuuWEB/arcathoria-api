package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.AuthenticatedAccountDTO;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

import java.util.UUID;

import static com.arcathoria.account.AccountDTOMapper.toAccountDTO;
import static com.arcathoria.account.AccountDTOMapper.toAuthenticatedAccountDTO;

public class AccountQueryFacade {

    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final CheckEmailExistsUseCase checkEmailExistsUseCase;
    private final ValidateAccountCredentials validateAccountCredentials;

    AccountQueryFacade(final GetAccountByIdUseCase getAccountByIdUseCase,
                       final CheckEmailExistsUseCase checkEmailExistsUseCase,
                       final ValidateAccountCredentials validateAccountCredentials
    ) {
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.checkEmailExistsUseCase = checkEmailExistsUseCase;
        this.validateAccountCredentials = validateAccountCredentials;
    }

    public AccountDTO getById(final UUID accountId) {
        return toAccountDTO(getAccountByIdUseCase.execute(new AccountId(accountId)));
    }

    public AuthenticatedAccountDTO authenticatedByEmailAndRawPassword(final String email, final String rawPassword) {
        return toAuthenticatedAccountDTO(validateAccountCredentials.validate(new Email(email), new HashedPassword(rawPassword)));
    }

    public void checkWhetherEmailIsExists(final String email) {
        checkEmailExistsUseCase.execute(new Email(email));
    }
}
