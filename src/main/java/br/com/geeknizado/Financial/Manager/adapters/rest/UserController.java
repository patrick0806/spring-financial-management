package br.com.geeknizado.Financial.Manager.adapters.rest;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.UserDTO;
import br.com.geeknizado.Financial.Manager.adapters.rest.openapi.UserOpenApi;
import br.com.geeknizado.Financial.Manager.internal.interector.CreateUserUseCase;
import br.com.geeknizado.Financial.Manager.internal.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/users")
public class UserController implements UserOpenApi {
    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDTO userDTO) {
        var user = createUserUseCase.execute(userDTO);
        return ResponseEntity.status(201).body(UserMapper.INSTANCE.map(user));
    }
}
