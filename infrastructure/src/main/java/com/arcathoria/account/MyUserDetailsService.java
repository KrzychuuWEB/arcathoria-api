package com.arcathoria.account;

import com.arcathoria.account.vo.Email;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final AccountQueryRepositoryAdapter accountQueryRepositoryAdapter;

    MyUserDetailsService(final AccountQueryRepositoryAdapter accountQueryRepositoryAdapter) {
        this.accountQueryRepositoryAdapter = accountQueryRepositoryAdapter;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Account account = accountQueryRepositoryAdapter.findByEmail(new Email(username))
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        return new MyUserDetails(account);
    }
}
