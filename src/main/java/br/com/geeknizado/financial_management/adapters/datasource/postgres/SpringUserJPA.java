package br.com.geeknizado.financial_management.adapters.datasource.postgres;

import br.com.geeknizado.financial_management.internal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringUserJPA extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}