package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Platform;

public interface PlatformRepository extends CrudRepository<Platform, Long> {

    @RestResource(path="find")
    Iterable<Platform> findByName(@Param("name") String name);
}
