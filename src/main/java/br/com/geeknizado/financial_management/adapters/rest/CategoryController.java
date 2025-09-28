package br.com.geeknizado.financial_management.adapters.rest;

import br.com.geeknizado.financial_management.adapters.rest.dtos.CategoryDTO;
import br.com.geeknizado.financial_management.adapters.rest.dtos.CreateCategoryDTO;
import br.com.geeknizado.financial_management.adapters.rest.mapper.CategoryMapper;
import br.com.geeknizado.financial_management.adapters.rest.openapi.CategoryOpenApi;
import br.com.geeknizado.financial_management.internal.interaction.category.CreateCategoryUseCase;
import br.com.geeknizado.financial_management.internal.interaction.category.ListCategoriesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/categories")
public class CategoryController implements CategoryOpenApi {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(CreateCategoryDTO categoryDTO) {
        var category = createCategoryUseCase.execute(categoryDTO);
        return ResponseEntity.status(201).body(CategoryMapper.INSTANCE.map(category));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories() {
        var categoires = listCategoriesUseCase.execute();
        return ResponseEntity.ok(CategoryMapper.INSTANCE.map(categoires));
    }
}
