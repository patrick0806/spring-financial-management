package br.com.geeknizado.Financial.Manager.internal.repository;

import br.com.geeknizado.Financial.Manager.internal.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category c);
    Optional<Category> findById(String id);
    Optional<Category> findByName(String name);
    List<Category> list();
}
