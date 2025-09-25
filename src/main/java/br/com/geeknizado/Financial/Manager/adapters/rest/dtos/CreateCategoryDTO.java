package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDTO(
       @NotBlank String name,
       @NotBlank String color,
       TransactionType type
) {
}
