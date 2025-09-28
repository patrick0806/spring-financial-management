package br.com.geeknizado.financial_management.adapters.rest.dtos;

import java.math.BigDecimal;

public record BalanceSummaryDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        BigDecimal nextMonthExpenses
) {
}
