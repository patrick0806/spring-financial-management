package br.com.geeknizado.financial_management.adapters.datasource.postgres;

import br.com.geeknizado.financial_management.adapters.datasource.postgres.model.BalanceSummary;
import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringTransactionJPA extends JpaRepository<Transaction, UUID> {
    void deleteByGroupId(UUID groupId);

    List<Transaction> findByGroupId(UUID groupId);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE (:transactionType IS NULL OR t.type = :transactionType)
              AND t.user.id = :userId
              AND MONTH(t.transactionDate) = :month
              AND YEAR(t.transactionDate) = :year
            ORDER BY t.transactionDate DESC
            """)
    List<Transaction> list(
            @Param("transactionType") TransactionType transactionType,
            @Param("month") int month,
            @Param("year") int year,
            @Param("userId") UUID userId
    );

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE (:transactionType IS NULL OR t.type = :transactionType)
              AND t.user.id = :userId
              AND MONTH(t.transactionDate) = :month
              AND YEAR(t.transactionDate) = :year
            ORDER BY t.transactionDate DESC
            LIMIT 10
            """)
    List<Transaction> listLastExpenses(
            @Param("transactionType") TransactionType transactionType,
            @Param("month") int month,
            @Param("year") int year,
            @Param("userId") UUID userId
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.groupId = :groupId AND t.transactionDate >= :startOfMonth")
    void deleteFromMonthOnwards(@Param("groupId") UUID groupId,
                                @Param("startOfMonth") OffsetDateTime startOfMonth);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.isRecurring = true " +
            "AND MONTH(t.transactionDate) = :month " +
            "AND YEAR(t.transactionDate) = :year")
    List<Transaction> findRecurringTransactions(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT COUNT(t) > 0 FROM Transaction t " +
            "WHERE t.groupId = :groupId " +
            "AND t.isRecurring = true " +
            "AND MONTH(t.transactionDate) = :month " +
            "AND YEAR(t.transactionDate) = :year")
    boolean existsByGroupIdAndMonthAndYear(@Param("groupId") UUID groupId,
                                           @Param("month") Integer month,
                                           @Param("year") Integer year);

    @Query("""
            SELECT new br.com.geeknizado.financial_management.adapters.datasource.postgres.model.BalanceSummary(
                        COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.value ELSE 0 END), 0),
                        COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.value ELSE 0 END), 0),
                        COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.value ELSE -t.value END), 0)
            )FROM Transaction t
                     WHERE t.user.id = :userId
                       AND MONTH(t.transactionDate) = :month
                       AND YEAR(t.transactionDate) = :year
            """)
    BalanceSummary getMonthlyBalance(
            @Param("userId") UUID userId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );


    @Query("""
    SELECT COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.value ELSE 0 END), 0)
    FROM Transaction t
    WHERE t.user.id = :userId
      AND MONTH(t.transactionDate) = :month
      AND YEAR(t.transactionDate) = :year
    """)
    BigDecimal getMonthlyTotalExpenses(
            @Param("userId") UUID userId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
}
