package br.com.geeknizado.financial_management.internal.interaction.category;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.financial_management.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase {
    private final CategoryRepository repository;

    public CreateCategoryUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category execute(CreateCategoryDTO categoryDTO){
        var alreadyExists = this.repository.findByName(categoryDTO.name());
        if(alreadyExists.isPresent()){
            throw new AlreadyExistsException(String.format("Already exists a category with this name: %s", categoryDTO.name()));
        }

        var category = Category.builder()
                .name(categoryDTO.name())
                .color(categoryDTO.color())
                .type(categoryDTO.type())
                .isActive(true)
                .build();

        return this.repository.save(category);
    }
}
