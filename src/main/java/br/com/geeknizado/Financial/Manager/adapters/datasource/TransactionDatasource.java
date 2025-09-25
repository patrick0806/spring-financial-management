package br.com.geeknizado.Financial.Manager.adapters.datasource;

import br.com.geeknizado.Financial.Manager.adapters.datasource.postgres.SpringTransactionJPA;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionDatasource implements TransactionRepository {
    private final SpringTransactionJPA repository;

    public TransactionDatasource(SpringTransactionJPA repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction t) {
        return this.repository.save(t);
    }

    @Override
    public Optional<Transaction> findById(String transactionId) {
        return this.repository.findById(UUID.fromString(transactionId));
    }

    @Override
    public List<Transaction> list(TransactionType transactionType, Integer month, Integer year) {
        return this.repository.list(transactionType, month, year);
    }

    @Override
    public void delete(Transaction transaction) {
            this.repository.delete(transaction);
    }

    @Override
    public void deleteByGroupId(UUID groupId){
        var relatedTransactions = this.repository.findByGroupId(groupId);

        var startOfCurrentMonth = OffsetDateTime.now()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay(OffsetDateTime.now().getOffset())
                .toOffsetDateTime();

        relatedTransactions.stream()
                .filter(t -> !t.getTransactionDate().isBefore(startOfCurrentMonth))
                .forEach(this.repository::delete);
    }
}
