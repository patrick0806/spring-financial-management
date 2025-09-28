package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.financial_management.internal.interaction.auth.RegistrationUseCase;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationUseCaseTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private RegistrationUseCase registrationUseCase;

    @Test
    void shouldRegisterNewUser() {
        var dto = new CreateUserDTO("Patrick", "patrick@test.com", "123456");

        when(repository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = registrationUseCase.execute(dto);

        assertThat(result.getName()).isEqualTo("Patrick");
        assertThat(result.getEmail()).isEqualTo("patrick@test.com");
        assertThat(result.getPassword()).isNotEqualTo("123456"); // deve estar encriptada
        assertThat(result.getIsActive()).isTrue();
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        var dto = new CreateUserDTO("Patrick", "patrick@test.com", "123456");

        when(repository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistsException.class,
                () -> registrationUseCase.execute(dto));
    }
}
