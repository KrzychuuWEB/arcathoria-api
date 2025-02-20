package com.arcathoria.account;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class AccountRepositoryAdapterTest {

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Test
    void should_save_and_find_account_by_email() {
        
    }
}