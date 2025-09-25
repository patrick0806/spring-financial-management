package br.com.geeknizado.Financial.Manager.internal.interector;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.Financial.Manager.bootstrap.exception.customException.AlreadyExistsException;
import br.com.geeknizado.Financial.Manager.internal.model.Category;
import br.com.geeknizado.Financial.Manager.internal.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase {
    private CategoryRepository repository;

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
                .build();

        return this.repository.save(category);
    }
}
