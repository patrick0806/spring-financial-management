package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.BusinessException;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.financial_management.internal.interaction.transaction.DeleteTransactionUseCase;
import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteTransactionUseCaseTest {

    private TransactionRepository repository;
    private DeleteTransactionUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionRepository.class);
        useCase = new DeleteTransactionUseCase(repository);
    }

    @Test
    void shouldThrowNotFoundWhenTransactionDoesNotExist() {
        var transactionId = UUID.randomUUID().toString();
        when(repository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.execute(transactionId, false, false));

        verify(repository, never()).delete(any());
        verify(repository, never()).deleteByGroupId(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenTransactionIsFromPastMonth() {
        var transactionId = UUID.randomUUID().toString();
        var transaction = Transaction.builder()
                .id(UUID.fromString(transactionId))
                .transactionDate(OffsetDateTime.now().minusMonths(1))
                .build();
        when(repository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThrows(BusinessException.class,
                () -> useCase.execute(transactionId, false, false));

        verify(repository, never()).delete(any());
        verify(repository, never()).deleteByGroupId(any());
    }

    @Test
    void shouldDeleteSingleTransactionWhenNotRecurringAndNoInstallments() {
        var transactionId = UUID.randomUUID().toString();
        var transaction = Transaction.builder()
                .id(UUID.fromString(transactionId))
                .transactionDate(OffsetDateTime.now().plusDays(1))
                .build();
        when(repository.findById(transactionId)).thenReturn(Optional.of(transaction));

        useCase.execute(transactionId, false, false);

        verify(repository).delete(transaction);
        verify(repository, never()).deleteByGroupId(any());
    }

    @Test
    void shouldDeleteByGroupIdWhenRecurringOrInstallments() {
        var transactionId = UUID.randomUUID().toString();
        var groupId = UUID.randomUUID();
        var transaction = Transaction.builder()
                .id(UUID.fromString(transactionId))
                .groupId(groupId)
                .transactionDate(OffsetDateTime.now().plusDays(1))
                .build();
        transaction.setTransactionDate(OffsetDateTime.now().plusDays(1));
        when(repository.findById(transactionId)).thenReturn(Optional.of(transaction));

        //Validate recurring
        useCase.execute(transactionId, true, false);

        verify(repository).deleteByGroupId(groupId);

        // Validate installments
        reset(repository);
        when(repository.findById(transactionId)).thenReturn(Optional.of(transaction));

        useCase.execute(transactionId, false, true);

        verify(repository).deleteByGroupId(groupId);
    }
}
