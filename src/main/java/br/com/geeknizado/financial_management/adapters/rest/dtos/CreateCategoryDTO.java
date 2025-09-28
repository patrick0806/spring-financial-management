package br.com.geeknizado.financial_management.adapters.rest.dtos;

import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;

public record CreateCategoryDTO(
        String name,
        String color,
        TransactionType type
) {
}
