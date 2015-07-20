package com.miwashi.repositories;

import com.miwashi.model.Platform;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PlatformRepository extends CrudRepository<Platform, Long> {

    @RestResource(path="find")
    Iterable<Platform> findByName(@Param("name") String name);
}
