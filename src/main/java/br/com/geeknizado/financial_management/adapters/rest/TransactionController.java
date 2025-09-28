package br.com.geeknizado.financial_management.adapters.rest;

import br.com.geeknizado.financial_management.adapters.rest.dtos.BalanceSummaryDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.TransactionDTO;
import br.com.geeknizado.financial_management.adapters.rest.mapper.TransactionMapper;
import br.com.geeknizado.financial_management.adapters.rest.openapi.TransactionOpenApi;
import br.com.geeknizado.financial_management.internal.interaction.transaction.CreateTransactionUseCase;
import br.com.geeknizado.financial_management.internal.interaction.transaction.DeleteTransactionUseCase;
import br.com.geeknizado.financial_management.internal.interaction.transaction.ListLastExpenses;
import br.com.geeknizado.financial_management.internal.interaction.transaction.ListTransactionsUseCase;
import br.com.geeknizado.financial_management.internal.metrics.BalanceSummaryUseCase;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("v1/transactions")
public class TransactionController implements TransactionOpenApi {
    private final ListTransactionsUseCase listTransactionsUseCase;
    private final ListLastExpenses listLastExpenses;
    private final BalanceSummaryUseCase balanceSummaryUseCase;
    private final CreateTransactionUseCase createTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    public TransactionController(
            ListTransactionsUseCase listTransactionsUseCase,
            CreateTransactionUseCase createTransactionUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase,
            ListLastExpenses listLastExpenses,
            BalanceSummaryUseCase balanceSummaryUseCase
    ) {
        this.listTransactionsUseCase = listTransactionsUseCase;
        this.createTransactionUseCase = createTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
        this.listLastExpenses = listLastExpenses;
        this.balanceSummaryUseCase = balanceSummaryUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody @Valid CreateTransactionDTO transactionDTO) {
        var transaction = createTransactionUseCase.execute(transactionDTO);
        return ResponseEntity.status(201).body(TransactionMapper.INSTANCE.map(transaction));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> listTransactions(
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        var result = this.listTransactionsUseCase.execute(transactionType,month,year);
        return ResponseEntity.status(200).body(TransactionMapper.INSTANCE.map(result));
    }

    @Override
    @GetMapping("/latest-expenses")
    public ResponseEntity<List<TransactionDTO>> listLatestExpenses(
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        var result = this.listLastExpenses.execute(month,year);
        return ResponseEntity.status(200).body(TransactionMapper.INSTANCE.map(result));
    }

    @Override
    @GetMapping("/balance")
    public ResponseEntity<BalanceSummaryDTO> balanceSummary(
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        var summary = this.balanceSummaryUseCase.execute(month,year);
        return ResponseEntity.ok(summary);
    }

    @Override
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable String transactionId,
            @RequestParam(defaultValue = "false") Boolean deleteRecurring,
            @RequestParam(defaultValue = "false") Boolean deleteInstallments
    ) {
        this.deleteTransactionUseCase.execute(transactionId, deleteRecurring, deleteInstallments);
        return ResponseEntity.status(204).build();
    }
}
