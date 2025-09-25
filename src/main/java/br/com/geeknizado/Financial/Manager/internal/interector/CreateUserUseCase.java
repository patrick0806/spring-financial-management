package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
    private final UserRepository repository;

    public CreateUserUseCase(UserRepository repository) {
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
