package com.arcathoria.account;

import com.arcathoria.account.command.CreateAccountCommand;

interface RegisterUseCase {

    Account register(final CreateAccountCommand command);
}
