package br.com.geeknizado.Financial.Manager.internal.repository;

import br.com.geeknizado.Financial.Manager.internal.model.User;

import java.util.Optional;

public interface UserRepository {
    public User save(User user);
    public Optional<User> findByEmail(String email);
    public Optional<User> findById(String id);
}
