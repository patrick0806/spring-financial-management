package br.com.geeknizado.Financial.Manager.adapters.rest;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CategoryDTO;
import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.Financial.Manager.adapters.rest.openapi.CategoryOpenApi;
import br.com.geeknizado.Financial.Manager.internal.interector.CreateCategoryUseCase;
import br.com.geeknizado.Financial.Manager.internal.interector.ListCategoriesUseCase;
import br.com.geeknizado.Financial.Manager.internal.mapper.CategoryMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("v1/categories")
public class CategoryController implements CategoryOpenApi {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryDTO categoryDTO) {
        var category = this.createCategoryUseCase.execute(categoryDTO);
        return ResponseEntity.status(201).body(CategoryMapper.INSTANCE.map(category));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories() {
        var categories = this.listCategoriesUseCase.execute();
        return ResponseEntity.status(200).body(CategoryMapper.INSTANCE.map(categories));
    }
}
