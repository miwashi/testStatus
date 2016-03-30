package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Job;

public interface JobRepository extends CrudRepository<Job, Long> {

    @RestResource(path="find")
    Iterable<Job> findByName(@Param("name") String name);

    Iterable<Job> findById(long id);

    Iterable<Job> findByKey(String key);
}
