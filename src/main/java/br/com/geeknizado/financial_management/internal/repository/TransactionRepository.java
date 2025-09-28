package br.com.geeknizado.financial_management.internal.repository;

import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Optional<Transaction> findById(String transactionId);
    List<Transaction> list(TransactionType transactionType, Integer month, Integer year, UUID userId);
    Transaction save(Transaction t);
    void delete(Transaction transaction);
    void deleteByGroupId(UUID groupId);
    List<Transaction> findRecurringTransactions(Integer month, Integer year);
    boolean existsByGroupIdAndMonthAndYear(UUID groupId, Integer month, Integer year);
}
