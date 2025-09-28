package br.com.geeknizado.financial_management.adapters.jobs;

import br.com.geeknizado.financial_management.internal.interaction.transaction.InsertRecurringTransactionsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class InsertNextMonthRecurringJob {
    private static final Logger log = LoggerFactory.getLogger(InsertNextMonthRecurringJob.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final InsertRecurringTransactionsUseCase insertRecurringTransactionsUseCase;

    public InsertNextMonthRecurringJob(InsertRecurringTransactionsUseCase insertRecurringTransactionsUseCase) {
        this.insertRecurringTransactionsUseCase = insertRecurringTransactionsUseCase;
    }

    @Scheduled(cron ="0 0 0 L * *")
    public void inserNextMonthRecurring(){
        try{
            log.info("Start insert next month recurring transactions at {}", dateFormat.format(new Date()));
            insertRecurringTransactionsUseCase.execute(log);
            log.info("Recurring transactions inserted with success at {}", dateFormat.format(new Date()));
        }catch (Exception e) {
            log.error("Recurring transactions inserted with fail at {}", dateFormat.format(new Date()), e);
            throw new RuntimeException(e);
        }
    }
}
