package br.com.geeknizado.Financial.Manager.adapters.datasource.postgres;

import br.com.geeknizado.Financial.Manager.internal.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringCategoryJPA extends JpaRepository<Category, UUID> {
    Optional<Category> findByName(String name);
}
