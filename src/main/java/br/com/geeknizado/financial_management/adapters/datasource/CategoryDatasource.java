package br.com.geeknizado.financial_management.adapters.datasource;

import br.com.geeknizado.financial_management.adapters.datasource.postgres.SpringCategoryJPA;
import br.com.geeknizado.financial_management.internal.model.Category;
import br.com.geeknizado.financial_management.internal.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryDatasource implements CategoryRepository {
    private final SpringCategoryJPA repository;

    public CategoryDatasource(SpringCategoryJPA repository) {
        this.repository = repository;
    }

    @Override
    public Category save(Category c) {
        return this.repository.save(c);
    }

    @Override
    public Optional<Category> findById(String id) {
        return this.repository.findById(UUID.fromString(id));
    }

    @Override
    public Optional<Category> findByName(String name) {
        return this.repository.findByName(name);
    }

    @Override
    public List<Category> list() {
        return this.repository.findAll(Sort.by(Sort.Order.by("name")));
    }
}
