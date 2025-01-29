package com.arcathoria.account;

import com.arcathoria.account.vo.HashedPassword;

interface PasswordEncoder {

    HashedPassword encode(String password);

    boolean matches(String password, HashedPassword hashedPassword);
}
