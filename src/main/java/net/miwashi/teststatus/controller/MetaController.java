package net.miwashi.teststatus.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import net.miwashi.receiver.Notification;
import net.miwashi.receiver.Receiver;
import net.miwashi.teststatus.TestStatusApplication;
import net.miwashi.teststatus.model.Browser;
import net.miwashi.teststatus.model.Group;
import net.miwashi.teststatus.model.Platform;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Stats;
import net.miwashi.teststatus.model.Subject;
import net.miwashi.teststatus.repositories.BrowserRepository;
import net.miwashi.teststatus.repositories.PlatformRepository;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.ResultRepository;

@CrossOrigin
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

    @RequestMapping(value = "/api/meta/platforms", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/platforms", notes = "Returns a status")
    public Collection<Platform> getAllPlatforms() {
        Map<String,Platform> result =  new HashMap<String, Platform>();

        Iterable<Platform> patformIter = platformRepository.findAll();
        patformIter.forEach( platform -> {
            result.put(platform.getName(),  platform);
        });
        return result.values();
    }

    @RequestMapping(value = "/api/meta/browsers", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/browsers", notes = "Returns a status")
    public Collection<Browser> getAllBrowsers() {
    	Map<String,Browser> result =  new HashMap<String, Browser>();

        Iterable<Browser> browserIter = browserRepository.findAll();
        browserIter.forEach( browser -> {
            result.put(browser.getName(),  browser);
        });
        return result.values();
    }

    
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
    public Collection<Notification> getAllIncomplete() {
        Map<String, Notification> incomplete = Receiver.getIncompleteReports();
        return incomplete.values();
    }

    @RequestMapping(value = "/api/meta/stats", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/meta/stats", notes = "Returns a status")
    public Stats getStats() {
        Stats response = TestStatusApplication.getStats();

        return response;
    }
}
