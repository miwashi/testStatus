package com.miwashi.repositories;

import com.miwashi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Iterable<User> findByName(String name);
}
