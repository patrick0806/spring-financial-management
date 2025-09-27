package br.com.geeknizado.financial_management.adapters.datasource.postgres;

import br.com.geeknizado.financial_management.internal.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringCategoryJPA extends JpaRepository<Category, UUID> {
    Optional<Category> findByName(String name);
}