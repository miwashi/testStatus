package com.miwashi.repositories;

import com.miwashi.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface GroupRepository extends CrudRepository<Group, Long> {

    Iterable<Group> findById(long id);

    @RestResource(path="find")
    Iterable<Group> findByName(@Param("name") String name);
}
