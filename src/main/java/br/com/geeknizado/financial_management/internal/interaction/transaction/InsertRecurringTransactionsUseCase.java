package br.com.geeknizado.financial_management.internal.interaction.transaction;

import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class InsertRecurringTransactionsUseCase {
    private final TransactionRepository repository;

    public InsertRecurringTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(Logger log){
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        // Target (M+2)
        int targetMonth = currentMonth + 2;
        int targetYear = currentYear;
        if (targetMonth > 12) {
            targetMonth -= 12;
            targetYear++;
        }

        // Source (M+1)
        int sourceMonth = currentMonth + 1;
        int sourceYear = currentYear;
        if (sourceMonth > 12) {
            sourceMonth -= 12;
            sourceYear++;
        }

        List<Transaction> baseTransactions = repository.findRecurringTransactions(sourceMonth, sourceYear);

        for (Transaction transaction : baseTransactions) {
            boolean alreadyExists = repository.existsByGroupIdAndMonthAndYear(
                    transaction.getGroupId(), targetMonth, targetYear);

            if (alreadyExists) {
                log.info("Transaction for groupId {} already exists in {}/{}. Skipping...",
                        transaction.getGroupId(), targetMonth, targetYear);
                continue;
            }

            Transaction newTransaction = Transaction.builder()
                    .groupId(transaction.getGroupId())
                    .description(transaction.getDescription())
                    .category(transaction.getCategory())
                    .user(transaction.getUser())
                    .isRecurring(true)
                    .transactionDate(transaction.getTransactionDate().plusMonths(1))
                    .type(transaction.getType())
                    .isRecurring(true)
                    .installments(transaction.getInstallments())
                    .installmentNumber(transaction.getInstallmentNumber())
                    .value(transaction.getValue())
                    .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                    .build();

            repository.save(newTransaction);
        }
    }
}
