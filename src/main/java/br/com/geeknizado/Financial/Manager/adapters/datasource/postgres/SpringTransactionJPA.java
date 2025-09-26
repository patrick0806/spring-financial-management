package br.com.geeknizado.Financial.Manager.adapters.datasource.postgres;

import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringTransactionJPA extends JpaRepository<Transaction, UUID> {
    void deleteByGroupId(UUID groupId);
    List<Transaction> findByGroupId(UUID groupId);

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE (:transactionType IS NULL OR t.type = :transactionType)
      AND MONTH(t.transactionDate) = :month
      AND YEAR(t.transactionDate) = :year
    ORDER BY t.transactionDate DESC
    """)
    List<Transaction> list(
            @Param("transactionType") TransactionType transactionType,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("DELETE FROM Transaction t WHERE t.groupId = :groupId AND t.transactionDate >= :startOfCurrentMonth")
    void deleteFromMonthOnwards(@Param("groupId") UUID groupId, @Param("startOfCurrentMonth") OffsetDateTime startOfCurrentMonth);
}
