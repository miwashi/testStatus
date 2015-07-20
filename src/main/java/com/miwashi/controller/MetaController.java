package com.miwashi.controller;

import com.miwashi.model.*;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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

    @RequestMapping(value = "/groups/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/groups/all", notes = "Returns a status")
    public Collection<Group> getAllGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            String requrement = aRquirement.getName();

            result.put(aRquirement.getTestGroup(),  new Group(aRquirement.getTestGroup()));
        });

        return result.values();
    }

    @RequestMapping(value = "/subgroups/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/subgroups/all", notes = "Returns a status")
    public Collection<Group> getAllSubGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            result.put(aRquirement.getTestSubGroup(),  new Group(aRquirement.getTestSubGroup()));
        });

        return result.values();
    }

    @RequestMapping(value = "/testsubjects/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/testsubjects/all", notes = "Returns a status")
    public Collection<Subject> getAllSubjects() {
        Map<String,Subject> result =  new HashMap<String, Subject>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            try {
                Subject aSubject;
                String key = aRquirement.getTestSubjectKey();
                if (result.containsKey(key)) {
                    System.out.println("1a");
                    aSubject = result.get(key);
                    System.out.println("2a");
                } else {
                    System.out.println("1b");
                    aSubject = new Subject(aRquirement.getTestSubject());
                    System.out.println("2b");
                    aSubject.setKey(key);
                    System.out.println("2c");
                    result.put(key, aSubject);
                    System.out.println("2d");
                }
                aSubject.add(aRquirement);
            }catch(Throwable any){
                System.out.println("******************************");
                System.out.println("******************************");
                System.out.println("******************************");
                System.out.println("******************************");
                System.out.println(aRquirement.getTestSubjectKey());
                System.out.println(any);
                System.out.println("******************************");
                System.out.println("******************************");
                System.out.println("******************************");
            }
        });

        return result.values();
    }
}
