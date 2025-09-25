package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private UUID id;
    private String name;
    private String color;
    private TransactionType type;
    private OffsetDateTime createdAt;
}
