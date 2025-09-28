package br.com.geeknizado.financial_management.adapters.datasource.postgres.model;

import java.math.BigDecimal;

public record BalanceSummary(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance
) {
}
