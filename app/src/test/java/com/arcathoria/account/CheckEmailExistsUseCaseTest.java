package com.arcathoria.account;

import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckEmailExistsUseCaseTest {

    @Mock
    private AccountQueryRepository accountQueryRepository;

    @InjectMocks
    private CheckEmailExistsUseCase checkEmailExistsUseCase;

    @Test
    void should_return_false_if_email_is_not_exist() {
        when(accountQueryRepository.existsByEmail(any(Email.class))).thenReturn(false);

        boolean result = checkEmailExistsUseCase.execute(new Email("not_used@email.com"));

        assertThat(result).isFalse();
    }

    @Test
    void should_throw_EmailExistException_if_email_is_exist() {
        when(accountQueryRepository.existsByEmail(any(Email.class))).thenReturn(true);

        assertThatThrownBy(() -> checkEmailExistsUseCase.execute(new Email("used@email.com"))).isInstanceOf(EmailExistsException.class);
    }
}