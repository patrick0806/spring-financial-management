package br.com.geeknizado.financial_management.internal.interaction.auth;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationUseCase {
    private final UserRepository repository;

    public RegistrationUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(CreateUserDTO userDTO){
        var alreadyExists = this.repository.findByEmail(userDTO.email());
        if(alreadyExists.isPresent()){
            throw new AlreadyExistsException(String.format("Already exists a user with this email", userDTO.email()));
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.password());
        var user = User.builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(encryptedPassword)
                .isActive(true)
                .build();

        return this.repository.save(user);
    }
}
