package br.com.geeknizado.financial_management.adapters.rest.dtos;

import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTransactionDTO(
        TransactionType type,
        BigDecimal value,
        @NotEmpty String description,
        @NotBlank String categoryId,
        OffsetDateTime transactionDate,
        Boolean isRecurring,
        Integer installments
) {
}
