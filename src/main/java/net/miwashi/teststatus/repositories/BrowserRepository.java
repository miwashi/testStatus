package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Browser;

public interface BrowserRepository extends CrudRepository<Browser, Long> {

    @RestResource(path="find")
    Iterable<Browser> findByName(@Param("name") String name);
}
