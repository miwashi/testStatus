package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Subject;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @RestResource(path="find")
    Iterable<Subject> findByName(@Param("name") String name);

    Iterable<Subject> findById(long id);

    Iterable<Subject> findByKey(String key);
}
