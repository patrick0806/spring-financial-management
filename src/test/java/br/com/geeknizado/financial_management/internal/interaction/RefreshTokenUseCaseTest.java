package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.UnauthorizedException;
import br.com.geeknizado.financial_management.internal.interaction.auth.RefreshTokenUseCase;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

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

        ReflectionTestUtils.setField(refreshTokenUseCase, "secret", "test-secret");
        ReflectionTestUtils.setField(refreshTokenUseCase, "refreshSecret", "test-refresh-secret");
        ReflectionTestUtils.setField(refreshTokenUseCase, "expiration", 900L);
        ReflectionTestUtils.setField(refreshTokenUseCase, "refreshExpiration", 3600L);
    }

    @Test
    void shouldGenerateNewTokensOnValidRefresh() {
        String refreshToken = JWT.create()
                .withIssuer("financial-management")
                .withSubject(user.getId().toString())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .sign(Algorithm.HMAC256("test-refresh-secret"));

        when(repository.findById(user.getId().toString())).thenReturn(Optional.of(user));

        var result = refreshTokenUseCase.execute(refreshToken);

        assertThat(result).containsKeys("accessToken", "refreshToken");
    }

    @Test
    void shouldThrowWhenUserNotFoundOnRefresh() {
        String refreshToken = JWT.create()
                .withIssuer("financial-management")
                .withSubject(UUID.randomUUID().toString())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .sign(Algorithm.HMAC256("test-refresh-secret"));

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> refreshTokenUseCase.execute(refreshToken));
    }
}