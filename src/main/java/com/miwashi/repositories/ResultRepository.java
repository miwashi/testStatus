package com.miwashi.repositories;

import com.miwashi.model.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ResultRepository extends CrudRepository<Result, Long> {

    @RestResource(path="find")
    Iterable<Result> findById(@Param("id") long id);
    
    Iterable<Result> findByOldKey(@Param("oldKey") String oldKey);
}
