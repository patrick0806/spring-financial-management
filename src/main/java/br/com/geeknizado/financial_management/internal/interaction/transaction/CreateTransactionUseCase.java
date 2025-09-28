package br.com.geeknizado.financial_management.internal.interaction.transaction;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CreateTransactionUseCase {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository repository;

    public CreateTransactionUseCase(CategoryRepository categoryRepository, UserRepository userRepository, TransactionRepository repository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.repository = repository;
    }

    public Transaction execute(CreateTransactionDTO transactionDTO){
        if (transactionDTO.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transaction value must be greater than 0");
        }

        var user = userRepository.findById(transactionDTO.userId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %s not found", transactionDTO.userId())));

        var category = categoryRepository.findById(transactionDTO.categoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found", transactionDTO.categoryId())));

        var transactionDate = transactionDTO.transactionDate() != null ? transactionDTO.transactionDate() : OffsetDateTime.now();
        var groupId = UUID.randomUUID();
        var isRecurring = transactionDTO.isRecurring() == true;
        var hasInstallments = transactionDTO.installments() > 0;

        if(!isRecurring && !hasInstallments){
            return repository.save(buildTransaction(transactionDTO, category, user,
                    transactionDate, null, false, 0, 0));
        }

        var transactions = new ArrayList<Transaction>();
        if(isRecurring){
            for (int i = 0; i < 2; i++) {
                transactions.add(repository.save(buildTransaction(
                        transactionDTO,
                        category,
                        user,
                        transactionDate.plusMonths(i),
                        groupId,
                        true,
                        0,
                        0
                )));
            }
            return transactions.getFirst();
        }

        for(int i =0; i< transactionDTO.installments(); i++){
            transactions.add(repository.save(buildTransaction(
                    transactionDTO,
                    category,
                    user,
                    transactionDate.plusMonths(i),
                    groupId,
                    false,
                    transactionDTO.installments(),
                    i + 1
            )));
        }

        return transactions.getFirst();
    }

    private Transaction buildTransaction(
            CreateTransactionDTO transactionDTO,
            Category category,
            User user,
            OffsetDateTime transactionDate,
            UUID groupId,
            boolean isRecurring,
            int installments,
            int installmentNumber
    ) {
        return Transaction.builder()
                .category(category)
                .groupId(groupId)
                .user(user)
                .type(transactionDTO.type())
                .description(transactionDTO.description())
                .isRecurring(isRecurring)
                .installments(installments)
                .installmentNumber(installmentNumber)
                .transactionDate(transactionDate)
                .value(transactionDTO.value())
                .build();
    }
}
