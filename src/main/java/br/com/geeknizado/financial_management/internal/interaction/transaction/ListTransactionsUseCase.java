package br.com.geeknizado.financial_management.internal.interaction.transaction;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.financial_management.bootstrap.security.SecurityUtils;
import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTransactionsUseCase {
    private final TransactionRepository repository;

    public ListTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> execute(TransactionType transactionType, Integer month, Integer year){
        if(month < 1 || month > 12){
            throw new BadRequestException("Invalid month value");
        }
        var userId = SecurityUtils.getCurrentUserId();
        return this.repository.list(transactionType, month, year, userId);
    }
}
