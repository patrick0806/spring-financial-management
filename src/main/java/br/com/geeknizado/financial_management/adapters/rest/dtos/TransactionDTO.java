package br.com.geeknizado.financial_management.adapters.rest.dtos;

import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        UUID groupId,
        TransactionType type,
        BigDecimal value,
        String description,
        CategoryDTO category,
        OffsetDateTime transactionDate,
        Boolean isRecurring,
        Integer installments,
        Integer installmentNumber,
        OffsetDateTime createdAt
) {
}
