package net.miwashi.teststatus.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.ApiOperation;

import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Subject;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.SubjectRepository;

@CrossOrigin
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
        mav.addObject("subject", subject);
        
        
        return mav;
    }
}
