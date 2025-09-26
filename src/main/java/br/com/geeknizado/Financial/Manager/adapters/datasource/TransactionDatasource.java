package br.com.geeknizado.Financial.Manager.adapters.datasource;

import br.com.geeknizado.Financial.Manager.adapters.datasource.postgres.SpringTransactionJPA;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
    @Transactional
    public void deleteByGroupId(UUID groupId){
        var startOfCurrentMonth = LocalDate.now(ZoneOffset.UTC)
                .withDayOfMonth(1)
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC);

        repository.deleteFromMonthOnwards(groupId, startOfCurrentMonth);
    }

    @Override
    public List<Transaction> findRecurringTransactions(Integer month, Integer year) {
        return this.repository.findRecurringTransactions(month,year);
    }

    @Override
    public boolean existsByGroupIdAndMonthAndYear(UUID groupId, Integer month, Integer year) {
        return this.repository.existsByGroupIdAndMonthAndYear(groupId,month,year);
    }
}
