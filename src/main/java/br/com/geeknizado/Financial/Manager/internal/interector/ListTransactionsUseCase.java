package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
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

        return this.repository.list(transactionType, month, year);
    }
}
