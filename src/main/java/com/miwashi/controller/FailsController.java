package com.miwashi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.miwashi.jsonapi.SimpleGroup;
import com.miwashi.jsonapi.summaries.GroupSummary;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.repositories.GroupRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.miwashi.repositories.SubjectRepository;
import com.wordnik.swagger.annotations.ApiOperation;

@Configuration
@RestController
public class FailsController {

    @Value("${configuration.default.browser}")
    private String DEFAULT_BROWSER = "";

    @Value("${configuration.default.platform}")
    private String DEFAULT_PLATFORM = "";

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    GroupRepository groupRepository;
    
    @Autowired
    SubjectRepository subjectRepository;
    

    @RequestMapping(value = "/api/fails/{groups}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/fails/{groups}", notes = "Returns a status")
    public GroupSummary getAllFailsForAPI(@PathParam(value = "groups") @PathVariable final String groups) {
    	GroupSummary testSummary = getGroupSummary();
    	return testSummary;
    }
    
    @RequestMapping("/fails/{groups}")
    public ModelAndView getAllFails(@PathParam(value = "groups") @PathVariable final String groups) {
        ModelAndView mav = new ModelAndView("fail");
        GroupSummary testSummary = getGroupSummary();
    	mav.addObject("summary", testSummary);
    	return mav;
    }
    
    GroupSummary getGroupSummary(){
    	GroupSummary summary = new GroupSummary();
    	List<Long> ids = new ArrayList<Long>();
    	Iterable<Result> resultIter = resultRepository.findAll();
    	for(Result result : resultIter){
    		if(result.isFail()){
    			if(!ids.contains(result.getRequirementId())){
    				ids.add(result.getRequirementId());
    			}
    		}
    	}
    	for(Long id : ids){
	    	Iterable<Requirement> requirementIter = requirementRepository.findById(id);
	    	for(Requirement requirement : requirementIter){
	    		summary.add(requirement);
	    	}
    	}
    	summary.setGroup(new SimpleGroup(0, "Fails"));
    	return summary;
    }
}
