package com.miwashi.repositories;

import com.miwashi.model.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @RestResource(path="find")
    Iterable<Subject> findByName(@Param("name") String name);

    Iterable<Subject> findById(long id);

    Iterable<Subject> findByKey(String key);
}
