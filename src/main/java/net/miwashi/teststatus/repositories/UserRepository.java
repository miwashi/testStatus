package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    @RestResource(path="find")
    Iterable<User> findByName(@Param("name") String name);
}
