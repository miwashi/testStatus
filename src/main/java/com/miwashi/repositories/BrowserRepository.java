package com.miwashi.repositories;

import com.miwashi.model.Browser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface BrowserRepository extends CrudRepository<Browser, Long> {

    @RestResource(path="find")
    Iterable<Browser> findByName(@Param("name") String name);
}
