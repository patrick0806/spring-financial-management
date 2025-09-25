package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.Financial.Manager.internal.model.Category;
import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import br.com.geeknizado.Financial.Manager.internal.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateCategoryUseCaseTest {
    private CategoryRepository repository;
    private CreateCategoryUseCase useCase;

    @BeforeEach
    void setUp(){
        repository = mock(CategoryRepository.class);
        useCase = new CreateCategoryUseCase(repository);
    }

    @Test
    void shouldCreateCategoryWhenNotExists() {
        var dto = new CreateCategoryDTO("Food", "hexcolor",TransactionType.EXPENSE);

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
