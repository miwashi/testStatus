package com.miwashi.controller;

import com.miwashi.TestStatusApplication;
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

    @RequestMapping(value = "/meta/groups", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/meta/groups", notes = "Returns a status")
    public Collection<Group> getAllGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            String requrement = aRquirement.getName();

            result.put(aRquirement.getTestGroup(),  new Group(aRquirement.getTestGroup()));
        });

        return result.values();
    }

    @RequestMapping(value = "/meta/subgroups", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/meta/subgroups", notes = "Returns a status")
    public Collection<Group> getAllSubGroups() {
        Map<String,Group> result =  new HashMap<String, Group>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            result.put(aRquirement.getTestSubGroup(),  new Group(aRquirement.getTestSubGroup()));
        });

        return result.values();
    }

    @RequestMapping(value = "/meta/testsubjects", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/meta/testsubjects", notes = "Returns a status")
    public Collection<Subject> getAllSubjects() {
        Map<String,Subject> result =  new HashMap<String, Subject>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            try {
                Subject aSubject;
                String key = aRquirement.getTestSubjectKey();
                if (result.containsKey(key)) {
                    aSubject = result.get(key);
                } else {
                    aSubject = new Subject(aRquirement.getTestSubject());
                    aSubject.setKey(key);
                    aSubject.setGroup(aRquirement.getTestGroup());
                    result.put(key, aSubject);
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

    @RequestMapping(value = "/meta/incomplete", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/meta/incomplete", notes = "Returns a status")
    public Collection<ResultReport> getAllIncomplete() {
        Map<String, ResultReport> incomplete = TestStatusApplication.getIncompleteReports();
        return incomplete.values();
    }
}
