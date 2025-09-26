package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class DeleteTransactionUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(String transactionId, Boolean isRecurring, Boolean hasInstallments){
        var transaction = this.repository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id: %s not found", transactionId)));

        var startOfCurrentMonth = OffsetDateTime.now()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay(OffsetDateTime.now().getOffset())
                .toOffsetDateTime();

        if (transaction.getTransactionDate().isBefore(startOfCurrentMonth)) {
            throw new BadRequestException("Transactions from past months cannot be deleted");
        }

        if(isRecurring || hasInstallments){
          this.repository.deleteByGroupId(transaction.getGroupId());
        }else{
            this.repository.delete(transaction);
        }
    }
}
