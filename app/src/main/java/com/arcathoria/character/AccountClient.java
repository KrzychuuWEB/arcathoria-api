package com.arcathoria.character;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.character.vo.AccountId;

interface AccountClient {

    AccountDTO getById(final AccountId accountId);
}
