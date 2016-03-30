package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Result;

public interface ResultRepository extends CrudRepository<Result, Long> {

    @RestResource(path="find")
    Iterable<Result> findById(@Param("id") long id);
    
    Iterable<Result> findByOldKey(@Param("oldKey") String oldKey);
}
