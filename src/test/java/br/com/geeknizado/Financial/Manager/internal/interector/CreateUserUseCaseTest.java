package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateUserUseCaseTest {
    private UserRepository repository;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        useCase = new CreateUserUseCase(repository);
    }

    @Test
    void shouldCreateUserWhenNotExists() {
        var dto = new CreateUserDTO("Patrick", "patrick@test.com", "123456");

        when(repository.findByEmail("patrick@test.com")).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        User result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals("Patrick", result.getName());
        assertEquals("patrick@test.com", result.getEmail());
        assertTrue(result.isActive());
        assertTrue(new BCryptPasswordEncoder().matches("123456", result.getPassword()));

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        var dto = new CreateUserDTO("Patrick", "patrick@test.com", "123456");

        when(repository.findByEmail("patrick@test.com")).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistsException.class, () -> useCase.execute(dto));
        verify(repository, never()).save(any());
    }
}
