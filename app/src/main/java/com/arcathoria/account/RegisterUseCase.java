package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;

interface RegisterUseCase {

    Account register(RegisterDTO registerDTO);
}
