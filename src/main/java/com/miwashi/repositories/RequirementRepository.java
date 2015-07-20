package com.miwashi.repositories;

import com.miwashi.model.Requirement;
import com.miwashi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface RequirementRepository extends CrudRepository<Requirement, Long> {

    @RestResource(path="find")
    Iterable<Requirement> findByName(@Param("name") String name);

    Iterable<Requirement> findById(long id);
}
