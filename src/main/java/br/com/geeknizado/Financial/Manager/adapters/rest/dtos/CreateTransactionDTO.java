package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTransactionDTO(
        TransactionType type,
        BigDecimal value,
        @NotBlank String description,
        @NotBlank String userId,
        @NotBlank String categoryId,
        OffsetDateTime transactionDate,
        Boolean isRecurring,
        Integer installments
) {

}
