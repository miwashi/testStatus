package com.miwashi.repositories;

import com.miwashi.model.Fail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface FailRepository extends CrudRepository<Fail, Long> {

	@RestResource(path="find")
    Iterable<Fail> findById(@Param("id") long id);

	Iterable<Fail> findByResultId(@Param("RESULT_ID")long id);
}
