package br.com.geeknizado.Financial.Manager.internal.jobs;

import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertNextMonthRecurringJobTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private InsertNextMonthRecurringJob job;

    @Test
    void shouldInsertRecurringTransactionsForNextTwoMonths() {
        // Given
        Transaction octoberTransaction = Transaction.builder()
                .groupId(UUID.randomUUID())
                .transactionDate(LocalDate.of(2025, 10, 5).atStartOfDay().atOffset(ZoneOffset.UTC))
                .description("Netflix")
                .isRecurring(true)
                .value(BigDecimal.valueOf(49.90))
                .build();

        when(repository.findRecurringTransactions(10, 2025))
                .thenReturn(List.of(octoberTransaction));


        when(repository.existsByGroupIdAndMonthAndYear(any(), eq(11), eq(2025)))
                .thenReturn(false);

        job.execute();

        verify(repository).save(argThat(tx ->
                tx.getTransactionDate().getMonthValue() == 11 &&
                        tx.getTransactionDate().getYear() == 2025 &&
                        tx.getDescription().equals("Netflix")
        ));
    }

    @Test
    void shouldDoNothingWhenNoRecurringTransactionsFound() {
        when(repository.findRecurringTransactions(10, 2025))
                .thenReturn(List.of());

        job.execute();


        verify(repository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldNotInsertDuplicateTransactions() {

        UUID groupId = UUID.randomUUID();
        Transaction octoberTransaction = Transaction.builder()
                .groupId(groupId)
                .transactionDate(LocalDate.of(2025, 10, 5).atStartOfDay().atOffset(ZoneOffset.UTC))
                .description("Spotify")
                .isRecurring(true)
                .value(BigDecimal.valueOf(19.90))
                .build();

        when(repository.findRecurringTransactions(10, 2025))
                .thenReturn(List.of(octoberTransaction));


        when(repository.existsByGroupIdAndMonthAndYear(eq(groupId), eq(11), eq(2025)))
                .thenReturn(true);


        job.execute();


        verify(repository, never()).save(any(Transaction.class));
    }
}