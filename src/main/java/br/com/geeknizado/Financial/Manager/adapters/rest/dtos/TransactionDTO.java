package br.com.geeknizado.Financial.Manager.adapters.rest.dtos;

import br.com.geeknizado.Financial.Manager.internal.model.Category;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransactionDTO {
    private UUID id;
    private UUID groupId;
    private TransactionType type;
    private BigDecimal value;
    private String description;
    private User user;
    private Category category;
    private OffsetDateTime transactionDate;
    private Boolean isRecurring;
    private Integer installments;
    private Integer installmentNumber;
    private OffsetDateTime createdAt;
}
