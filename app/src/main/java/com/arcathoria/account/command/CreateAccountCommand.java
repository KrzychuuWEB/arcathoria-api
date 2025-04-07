package com.arcathoria.account.command;

import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

public record CreateAccountCommand(Email email, HashedPassword password) {
}
