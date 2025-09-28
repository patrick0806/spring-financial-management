package br.com.geeknizado.financial_management.adapters.rest.openapi;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CategoryDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateCategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Categories")
public interface CategoryOpenApi {
    @Operation(summary = "Create category")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Create categories",
                    content = { @Content(schema = @Schema(implementation = CategoryDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<CategoryDTO> createCategory(CreateCategoryDTO categoryDTO);

    @Operation(summary = "List categories")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List categories",
                    content = { @Content(schema = @Schema(implementation = CategoryDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<List<CategoryDTO>> listCategories();
}
