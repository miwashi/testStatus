package net.miwashi.teststatus.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.User;

public interface RequirementRepository extends CrudRepository<Requirement, Long> {

    @RestResource(path="find")
    Iterable<Requirement> findByKey(@Param("KEY") String key);

    Iterable<Requirement> findById(long id);

    Iterable<Requirement> findByGroupId(@Param("GROUP_ID")long id);

    Iterable<Requirement> findBySubGroupId(@Param("SUBGROUP_ID")long id);
    
    Iterable<Requirement> findBySubjectId(@Param("SUBJECT_ID")long id);
}
