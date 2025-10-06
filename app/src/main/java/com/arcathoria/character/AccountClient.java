package com.arcathoria.character;

import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.vo.AccountId;

interface AccountClient {

    AccountView getById(final AccountId accountId);
}
