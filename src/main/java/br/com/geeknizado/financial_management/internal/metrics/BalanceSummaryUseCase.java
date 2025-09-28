package br.com.geeknizado.financial_management.internal.metrics;

import br.com.geeknizado.financial_management.adapters.rest.dtos.BalanceSummaryDTO;
import br.com.geeknizado.financial_management.bootstrap.security.SecurityUtils;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class BalanceSummaryUseCase {
    private final TransactionRepository repository;

    public BalanceSummaryUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public BalanceSummaryDTO execute(Integer month, Integer year){
        var userId = SecurityUtils.getCurrentUserId();
        var nextMonth = LocalDate.of(year, month, 1).plusMonths(1);
        var currentMonthBalance = this.repository.getMonthlyBalance(userId, month, year);

        var nextMonthExpenses = this.repository.getMonthlyTotalExpenses(userId,nextMonth.getMonth().getValue(), nextMonth.getYear());

        return new BalanceSummaryDTO(currentMonthBalance.totalIncome(), currentMonthBalance.totalExpense(), currentMonthBalance.balance(), nextMonthExpenses);
    }
}
