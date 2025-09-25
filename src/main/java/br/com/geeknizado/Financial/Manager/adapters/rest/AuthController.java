package br.com.geeknizado.Financial.Manager.adapters.rest;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.LoginRequest;
import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.LoginResponse;
import br.com.geeknizado.Financial.Manager.adapters.rest.openapi.AuthOpenApi;
import br.com.geeknizado.Financial.Manager.internal.interector.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthController implements AuthOpenApi {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        var accessToken = loginUseCase.execute(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

}
