package com.miwashi.controller;

import com.miwashi.model.Browser;
import com.miwashi.model.Group;
import com.miwashi.model.Platform;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.model.Subject;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.miwashi.repositories.SubjectRepository;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Configuration
@RestController
public class SubjectController {

	@Autowired
    SubjectRepository subjectRepository;
	
	@Autowired 
	RequirementRepository requirementRepository;

    @RequestMapping(value = "/api/subject/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/subjects/all", notes = "Returns a status")
    public List<Subject> getAllSubjects() {
        List<Subject> result =  new ArrayList<Subject>();

        return result;
    }

    @RequestMapping(value = "/api/subject/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/subjects/{id}", notes = "Returns a status")
    public Subject getSubject(@PathParam(value = "Path id") @PathVariable final Long id) {
    	
    	Subject subject = null;
        Iterable<Subject> subjectIter = subjectRepository.findById(id);
        if(subjectIter.iterator().hasNext()){
        	subject = subjectIter.iterator().next();
        }
        
        Iterable<Requirement> requirementIter = requirementRepository.findBySubjectId(id);
        for(Requirement requirement : requirementIter){
        	subject.add(requirement);
        }
        
        return subject;
    }
    
    @RequestMapping(value = "/subject/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getSubjectById(@PathParam(value = "Path id") @PathVariable final Long id) {
        ModelAndView mav = new ModelAndView("subject");

        Subject subject = null;
        Iterable<Subject> subjectIter = subjectRepository.findById(id);
        if(subjectIter.iterator().hasNext()){
        	subject = subjectIter.iterator().next();
        	
        	Iterable<Requirement> requirementIter = requirementRepository.findBySubjectId(id);
            for(Requirement requirement : requirementIter){
            	subject.add(requirement);
            }
        }
        
        List<Requirement> results = subject.getRequirements();
    	Collections.sort(results, new Comparator<Requirement>() {
            @Override
            public int compare(Requirement req1, Requirement req2) {
                if (req1 == null || req2 == null || req1.getLastTested() == null || req2.getLastTested() == null) {
                    return 0;
                }
                return req2.getLastTested().compareTo(req1.getLastTested());
            }
        });
        
        mav.addObject("subject", subject);
        
        return mav;
    }
}
