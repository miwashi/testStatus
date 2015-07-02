package com.miwashi.repositories;

import com.miwashi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserRepository extends CrudRepository<User, Long> {

    @RestResource(path="find")
    Iterable<User> findByName(@Param("name") String name);
}
