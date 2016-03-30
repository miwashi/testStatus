package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Fail;

public interface FailRepository extends CrudRepository<Fail, Long> {

	@RestResource(path="find")
    Iterable<Fail> findById(@Param("id") long id);

	Iterable<Fail> findByResultId(@Param("RESULT_ID")long id);
}
