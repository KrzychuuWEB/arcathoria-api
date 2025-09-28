package com.arcathoria.character;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.vo.AccountId;

interface AccountClient {

    AccountDTO getById(final AccountId accountId);
}
