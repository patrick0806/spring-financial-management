package br.com.geeknizado.financial_management.internal.interaction;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.financial_management.internal.interaction.category.CreateCategoryUseCase;
import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CreateCategoryUseCase useCase;

    @Test
    void shouldCreateCategoryWhenNotExists() {
        var dto = new CreateCategoryDTO("Food", "hexcolor", TransactionType.EXPENSE);

        when(repository.findByName("Food")).thenReturn(Optional.empty());
        when(repository.save(any(Category.class))).thenAnswer(invocation -> {
            Category c = invocation.getArgument(0);
            c.setId(java.util.UUID.randomUUID());
            return c;
        });

        Category result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals("Food", result.getName());
        assertEquals(TransactionType.EXPENSE, result.getType());
        verify(repository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        var dto = new CreateCategoryDTO("Food", "hexcolor", TransactionType.EXPENSE);

        when(repository.findByName("Food")).thenReturn(Optional.of(Category.builder().name("FOOD").build()));

        assertThrows(AlreadyExistsException.class, () -> useCase.execute(dto));
        verify(repository, never()).save(any());
    }
}
