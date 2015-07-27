package com.miwashi.controller;

import com.miwashi.TestStatusApplication;
import com.miwashi.model.*;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Configuration
@RestController
public class MetaController {

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    BrowserRepository browserRepository;

    @Autowired
    PlatformRepository platformRepository;

    @RequestMapping(value = "/api/meta/groups", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/groups", notes = "Returns a status")
    public Collection<Group> getAllGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            String requrement = aRquirement.getKey();

            result.put(aRquirement.getGroup().getName(),  aRquirement.getGroup());
        });

        return result.values();
    }

    @RequestMapping(value = "/api/meta/subgroups", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/subgroups", notes = "Returns a status")
    public Collection<Group> getAllSubGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            result.put(aRquirement.getGroup().getName(),  aRquirement.getGroup());
        });

        return result.values();
    }

    @RequestMapping(value = "/api/meta/testsubjects", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/testsubjects", notes = "Returns a status")
    public Collection<Subject> getAllSubjects() {
        Map<String,Subject> result =  new HashMap<String, Subject>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            Subject aSubject;
            String key = aRquirement.getSubject().getKey();
            if (result.containsKey(key)) {
                aSubject = result.get(key);
            } else {
                aSubject = aRquirement.getSubject();
                result.put(key, aSubject);
            }
            aSubject.add(aRquirement);
        });

        return result.values();
    }

    @RequestMapping(value = "/api/meta/incomplete", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/incomplete", notes = "Returns a status")
    public Collection<ResultReport> getAllIncomplete() {
        Map<String, ResultReport> incomplete = TestStatusApplication.getIncompleteReports();
        return incomplete.values();
    }

    @RequestMapping(value = "/api/meta/stats", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/stats", notes = "Returns a status")
    public Stats getStats() {
        Stats response = TestStatusApplication.getStats();

        return response;
    }
}
