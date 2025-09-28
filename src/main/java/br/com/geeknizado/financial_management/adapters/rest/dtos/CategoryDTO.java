package br.com.geeknizado.financial_management.adapters.rest.dtos;

import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;

import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name,
        String color,
        TransactionType type,
        Boolean isActive
) {
}