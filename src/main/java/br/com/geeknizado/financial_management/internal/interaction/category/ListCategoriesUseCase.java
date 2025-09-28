package br.com.geeknizado.financial_management.internal.interaction.category;

import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoriesUseCase {
    private final CategoryRepository repository;

    public ListCategoriesUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> execute(){
        return this.repository.list();
    }
}
