package br.com.geeknizado.Financial.Manager.adapters.datasource.postgres;

import br.com.geeknizado.Financial.Manager.internal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringUserJPA extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
