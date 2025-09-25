package br.com.geeknizado.Financial.Manager.adapters.rest.openapi;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.LoginRequest;
import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth")
public interface AuthOpenApi {
    @Operation(summary = "Authenticate a user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated",
                    content = { @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity authenticate(LoginRequest loginRequest);
}