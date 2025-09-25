package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.repository.CategoryRepository;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
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

    public Transaction execute(CreateTransactionDTO transactionDTO) {
        var user = userRepository.findById(transactionDTO.userId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %s not found", transactionDTO.userId())));

        var category = categoryRepository.findById(transactionDTO.categoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id: %s not found", transactionDTO.categoryId())));

        if (transactionDTO.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transaction value must be greater than 0");
        }

        var transactionDate = transactionDTO.transactionDate() != null ? transactionDTO.transactionDate() : OffsetDateTime.now();
        var groupId = UUID.randomUUID();
        if (transactionDTO.isRecurring() == false && transactionDTO.installments() == 0) {
            return this.repository.save(Transaction.builder()
                    .category(category)
                    .groupId(groupId)
                    .user(user)
                    .type(transactionDTO.type())
                    .description(transactionDTO.description())
                    .isRecurring(false)
                    .installments(0)
                    .installmentNumber(0)
                    .transactionDate(transactionDate)
                    .value(transactionDTO.value())
                    .build());
        }

        var transactions = new ArrayList<Transaction>();
        if(transactionDTO.isRecurring()){
            for(int i=0; i <2; i++){
               transactions.add( this.repository.save(Transaction.builder()
                       .category(category)
                       .groupId(groupId)
                       .user(user)
                       .type(transactionDTO.type())
                       .description(transactionDTO.description())
                       .isRecurring(true)
                       .installments(0)
                       .installmentNumber(0)
                       .transactionDate(transactionDate.plusMonths(i))
                       .value(transactionDTO.value())
                       .build()));
            }

            return transactions.getFirst();
        }

        for(int i =0; i< transactionDTO.installments(); i++){
            transactions.add( this.repository.save(Transaction.builder()
                    .category(category)
                    .groupId(groupId)
                    .user(user)
                    .type(transactionDTO.type())
                    .description(transactionDTO.description())
                    .isRecurring(false)
                    .installments(transactionDTO.installments())
                    .installmentNumber(i + 1 )
                    .transactionDate(transactionDate.plusMonths(i))
                    .value(transactionDTO.value())
                    .build()));
        }

        return transactions.getFirst();
    }
}
