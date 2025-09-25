package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.Financial.Manager.internal.model.Category;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import br.com.geeknizado.Financial.Manager.internal.repository.CategoryRepository;
import br.com.geeknizado.Financial.Manager.internal.repository.TransactionRepository;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateTransactionUseCaseTest {

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private CreateTransactionUseCase useCase;

    @BeforeEach
    void setup() {
        categoryRepository = mock(CategoryRepository.class);
        userRepository = mock(UserRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        useCase = new CreateTransactionUseCase(categoryRepository, userRepository, transactionRepository);
    }

    @Test
    void shouldCreateSimpleTransaction() {
        var user = new User();
        var category = Category.builder().build();

        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new CreateTransactionDTO(
                TransactionType.INCOME,
                BigDecimal.valueOf(1000),
                "Salary",
                "userId",
                "categoryId",
                OffsetDateTime.now(),
                false,
                0
        );

        Transaction result = useCase.execute(dto);

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(result.isRecurring()).isFalse();
        assertThat(result.getInstallments()).isEqualTo(0);
        assertThat(result.getInstallmentNumber()).isEqualTo(0);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldCreateRecurringTransactionsForTwoMonths() {
        var user = new User();
        var category = Category.builder().build();

        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new CreateTransactionDTO(
                TransactionType.EXPENSE,
                BigDecimal.TEN,
                "Netflix",
                "userId",
                "categoryId",
                OffsetDateTime.now(),
                true,
                0
        );

        Transaction result = useCase.execute(dto);

        assertThat(result).isNotNull();
        assertThat(result.isRecurring()).isTrue();
        assertThat(result.getDescription()).isEqualTo("Netflix");

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(captor.capture());

        var savedTransactions = captor.getAllValues();
        assertThat(savedTransactions.get(0).getTransactionDate())
                .isBefore(savedTransactions.get(1).getTransactionDate());
    }

    @Test
    void shouldCreateInstallmentTransactions() {
        var user = new User();
        var category = Category.builder().build();

        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new CreateTransactionDTO(
                TransactionType.EXPENSE,
                BigDecimal.valueOf(3000),
                "Notebook",
                "userId",
                "categoryId",
                OffsetDateTime.now(),
                false,
                3
        );

        Transaction result = useCase.execute(dto);

        assertThat(result).isNotNull();
        assertThat(result.isRecurring()).isFalse();

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(3)).save(captor.capture());

        var savedTransactions = captor.getAllValues();
        assertThat(savedTransactions).isNotNull();
        assertThat(savedTransactions.get(0).getInstallmentNumber()).isEqualTo(1);
        assertThat(savedTransactions.get(1).getInstallmentNumber()).isEqualTo(2);
        assertThat(savedTransactions.get(2).getInstallmentNumber()).isEqualTo(3);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById("userId")).thenReturn(Optional.empty());

        var dto = new CreateTransactionDTO(TransactionType.EXPENSE, BigDecimal.TEN, "Coffee", "userId", "categoryId", OffsetDateTime.now(),false, 0);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id: userId not found");
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        when(userRepository.findById("userId")).thenReturn(Optional.of(new User()));
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.empty());

        var dto = new CreateTransactionDTO(TransactionType.EXPENSE, BigDecimal.TEN, "Coffee", "userId", "categoryId",OffsetDateTime.now(), false, 0);

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category with id: categoryId not found");
    }

    @Test
    void shouldThrowWhenValueIsZeroOrNegative() {
        when(userRepository.findById("userId")).thenReturn(Optional.of(new User()));
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(Category.builder().build()));

        var dtoZero = new CreateTransactionDTO(TransactionType.EXPENSE, BigDecimal.ZERO, "Coffee", "userId", "categoryId",OffsetDateTime.now(), false, 0);
        var dtoNegative = new CreateTransactionDTO(TransactionType.EXPENSE, BigDecimal.valueOf(-5), "Coffee", "userId", "categoryId",OffsetDateTime.now(), false, 0);

        assertThatThrownBy(() -> useCase.execute(dtoZero))
                .isInstanceOf(BadRequestException.class);

        assertThatThrownBy(() -> useCase.execute(dtoNegative))
                .isInstanceOf(BadRequestException.class);
    }
}