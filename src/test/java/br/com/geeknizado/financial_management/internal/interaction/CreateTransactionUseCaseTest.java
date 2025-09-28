package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateTransactionDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.BadRequestException;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.NotFoundException;
import br.com.geeknizado.financial_management.internal.interaction.transaction.CreateTransactionUseCase;
import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.model.Transaction;
import br.com.geeknizado.financial_management.internal.model.User;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import br.com.geeknizado.financial_management.internal.repository.TransactionRepository;
import br.com.geeknizado.financial_management.internal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateTransactionUseCaseTest {

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private CreateTransactionUseCase createTransactionUseCase;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        userRepository = mock(UserRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        createTransactionUseCase = new CreateTransactionUseCase(categoryRepository, userRepository, transactionRepository);

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Patrick")
                .email("patrick@test.com")
                .isActive(true)
                .build();

        category = Category.builder()
                .id(UUID.randomUUID())
                .name("Food")
                .type(TransactionType.EXPENSE)
                .isActive(true)
                .build();
    }

    @Test
    void shouldCreateSimpleTransaction() {
        var dto = new CreateTransactionDTO(
                TransactionType.EXPENSE,
                new BigDecimal("100.0"),
                "Simple transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                false,
                0
        );

        when(userRepository.findById(user.getId().toString())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(category.getId().toString())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = createTransactionUseCase.execute(dto);

        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getValue()).isEqualByComparingTo("100.0");
        assertThat(result.isRecurring()).isFalse();
        assertThat(result.getInstallments()).isEqualTo(0);
        assertThat(result.getInstallmentNumber()).isEqualTo(0);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldCreateRecurringTransactions() {
        var dto = new CreateTransactionDTO(
                TransactionType.EXPENSE,
                new BigDecimal("100.0"),
                "Recurring transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                true,
                0
        );

        when(userRepository.findById(user.getId().toString())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(category.getId().toString())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction first = createTransactionUseCase.execute(dto);

        assertThat(first.isRecurring()).isTrue();
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void shouldCreateInstallmentTransactions() {
        var dto = new CreateTransactionDTO(TransactionType.EXPENSE,
                new BigDecimal("100.0"),
                "Simple transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                false,
                3
        );

        when(userRepository.findById(user.getId().toString())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(category.getId().toString())).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction first = createTransactionUseCase.execute(dto);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(3)).save(captor.capture());

        var saved = captor.getAllValues();
        for (int i = 0; i < saved.size(); i++) {
            assertThat(saved.get(i).getInstallmentNumber()).isEqualTo(i + 1);
            assertThat(saved.get(i).getInstallments()).isEqualTo(3);
        }

        assertThat(first).isEqualTo(saved.get(0));
    }

    @Test
    void shouldThrowBadRequestIfValueIsZeroOrNegative() {
        var dto = new CreateTransactionDTO(TransactionType.EXPENSE,
                new BigDecimal("0"),
                "Simple transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                false,
                0
        );

        assertThrows(BadRequestException.class, () -> createTransactionUseCase.execute(dto));
    }

    @Test
    void shouldThrowNotFoundIfUserNotFound() {
        var dto = new CreateTransactionDTO(TransactionType.EXPENSE,
                new BigDecimal("100.0"),
                "Simple transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                false,
                0);

        when(userRepository.findById(dto.userId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> createTransactionUseCase.execute(dto));
    }

    @Test
    void shouldThrowNotFoundIfCategoryNotFound() {
        var dto = new CreateTransactionDTO(
                TransactionType.EXPENSE,
                new BigDecimal("100.0"),
                "Simple transaction",
                user.getId().toString(),
                category.getId().toString(),
                OffsetDateTime.now(),
                false,
                0
        );

        when(userRepository.findById(user.getId().toString())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createTransactionUseCase.execute(dto));
    }
}
