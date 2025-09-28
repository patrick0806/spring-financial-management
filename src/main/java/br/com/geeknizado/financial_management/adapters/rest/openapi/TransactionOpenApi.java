package br.com.geeknizado.financial_management.adapters.rest.openapi;

import br.com.geeknizado.financial_management.adapters.rest.dtos.BalanceSummaryDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.TransactionDTO;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Transactions")
public interface TransactionOpenApi {
    @Operation(summary = "Create transaction")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Create transactions",
                    content = { @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<TransactionDTO> createTransaction(CreateTransactionDTO transactionDTO);

    @Operation(summary = "Delete transaction")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Create transactions",
                    content = { @Content(schema = @Schema(implementation = Void.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<Void> deleteTransaction(String transactionId, Boolean deleteRecurring, Boolean deleteInstallments);

    @Operation(summary = "List transactions")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Create transactions",
                    content = { @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<List<TransactionDTO>> listTransactions(TransactionType transactionType, Integer month, Integer year);

    @Operation(summary = "List last expenses")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List Expenses",
                    content = { @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<List<TransactionDTO>> listLatestExpenses(Integer month, Integer year);

    @Operation(summary = "List last expenses")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Balance summary",
                    content = { @Content(schema = @Schema(implementation = BalanceSummaryDTO.class), mediaType = "application/json") }
            ),
    })
    ResponseEntity<BalanceSummaryDTO> balanceSummary(Integer month, Integer year);
}
