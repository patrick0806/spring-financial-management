package br.com.geeknizado.Financial.Manager.internal.jobs;

import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class InsertNextMonthRecurringJob {
    private static final Logger log = LoggerFactory.getLogger(InsertNextMonthRecurringJob.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final TransactionRepository repository;

    public InsertNextMonthRecurringJob(TransactionRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron ="0 0 0 L * *")
    public void execute() {
        try{
            log.info("Start insert next month recurring transactions at {}", dateFormat.format(new Date()));

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
            log.info("Recurring transactions inserted with success at {}", dateFormat.format(new Date()));
        } catch (Exception e) {
            log.error("Recurring transactions inserted with fail at {}", dateFormat.format(new Date()), e);
            throw new RuntimeException(e);
        }
    }
}
