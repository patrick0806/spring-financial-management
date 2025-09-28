package br.com.geeknizado.financial_management.internal.interaction.transaction;

import br.com.geeknizado.financial_management.bootstrap.exception.customException.BusinessException;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DeleteTransactionUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(String transactionId, Boolean deleteRecurring, Boolean deleteInstallments){
        var transaction = this.repository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id: %s not found", transactionId)));

        var startOfCurrentMonth = OffsetDateTime.now()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay(OffsetDateTime.now().getOffset())
                .toOffsetDateTime();

        if (transaction.getTransactionDate().isBefore(startOfCurrentMonth)) {
            throw new BusinessException("Transactions from past months cannot be deleted");
        }

        if(deleteRecurring || deleteInstallments){
            this.repository.deleteByGroupId(transaction.getGroupId());
        }else{
            this.repository.delete(transaction);
        }
    }
}
