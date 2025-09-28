package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.internal.interaction.auth.LoginUseCase;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Patrick")
                .email("patrick@test.com")
                .password("encodedPassword")
                .isActive(true)
                .build();

        ReflectionTestUtils.setField(loginUseCase, "secret", "test-secret");
        ReflectionTestUtils.setField(loginUseCase, "refreshSecret", "test-refresh-secret");
        ReflectionTestUtils.setField(loginUseCase, "expiration", 900L);
        ReflectionTestUtils.setField(loginUseCase, "refreshExpiration", 3600L);
    }

    @Test
    void shouldGenerateTokensOnValidLogin() {
        when(userRepository.findByEmail("patrick@test.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);

        var result = loginUseCase.execute("patrick@test.com", "password");

        assertThat(result).containsKeys("accessToken", "refreshToken");
        assertThat(result.get("accessToken")).isNotBlank();
        assertThat(result.get("refreshToken")).isNotBlank();
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> loginUseCase.loadUserByUsername("notfound@test.com"));
    }
}
