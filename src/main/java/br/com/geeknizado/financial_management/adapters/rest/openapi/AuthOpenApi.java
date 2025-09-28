package br.com.geeknizado.financial_management.adapters.rest.openapi;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.LoginDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Auth")
public interface AuthOpenApi {
    @Operation(summary = "Login user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated",
                    content = { @Content(schema = @Schema(implementation = Map.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<Map<String, String>> login(LoginDTO loginRequest);

    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<UserDTO> Singin(CreateUserDTO dto);

    @Operation(summary = "Refresh Token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated",
                    content = { @Content(schema = @Schema(implementation = Map.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<Map<String, String>> refresh(String refreshToken);

}
