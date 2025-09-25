package br.com.geeknizado.Financial.Manager.adapters.datasource;

import br.com.geeknizado.Financial.Manager.adapters.datasource.postgres.SpringUserJPA;
import br.com.geeknizado.Financial.Manager.internal.model.User;
import br.com.geeknizado.Financial.Manager.internal.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserDatasource implements UserRepository {
    private final SpringUserJPA repository;

    public UserDatasource(SpringUserJPA repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return this.repository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return this.repository.findById(UUID.fromString(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }
}
