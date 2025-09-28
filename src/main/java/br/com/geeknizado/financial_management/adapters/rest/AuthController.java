package br.com.geeknizado.financial_management.adapters.rest;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.LoginDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.UserDTO;
import br.com.geeknizado.financial_management.adapters.rest.mapper.UserMapper;
import br.com.geeknizado.financial_management.adapters.rest.openapi.AuthOpenApi;
import br.com.geeknizado.financial_management.internal.interaction.auth.LoginUseCase;
import br.com.geeknizado.financial_management.internal.interaction.auth.RefreshTokenUseCase;
import br.com.geeknizado.financial_management.internal.interaction.auth.RegistrationUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/v1/auth")
public class AuthController implements AuthOpenApi {
    private final RegistrationUseCase registrationUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegistrationUseCase registrationUseCase, LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase) {
        this.registrationUseCase = registrationUseCase;
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO loginRequest) {
        var response = loginUseCase.execute(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserDTO> Singin(@RequestBody @Valid CreateUserDTO dto) {
        var user = registrationUseCase.execute(dto);
        return ResponseEntity.status(201).body(UserMapper.INSTANCE.map(user));
    }

    @Override
    @PostMapping("refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader(name = "refresh-token") String refreshToken) {
        var response = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(response);
    }
}
