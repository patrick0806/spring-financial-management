package br.com.geeknizado.Financial.Manager.internal.repository;

import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Optional<Transaction> findById(String transactionId);
    List<Transaction> list(TransactionType transactionType, Integer month, Integer year);
    Transaction save(Transaction t);
    void delete(Transaction transaction);
    void deleteByGroupId(UUID groupId);
}
