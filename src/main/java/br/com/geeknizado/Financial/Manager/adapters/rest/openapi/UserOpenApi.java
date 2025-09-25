package br.com.geeknizado.Financial.Manager.adapters.rest.openapi;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateUserDTO;
import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users")
public interface UserOpenApi {
    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated",
                    content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<UserDTO> createUser(CreateUserDTO userDTO);
}
