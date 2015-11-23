package com.miwashi.controller;

import com.miwashi.jsonapi.SimpleGroup;
import com.miwashi.jsonapi.SimplePackageWithRequirements;
import com.miwashi.jsonapi.SimpleTeam;
import com.miwashi.jsonapi.summaries.GroupSummary;
import com.miwashi.jsonapi.summaries.GroupsSummary;
import com.miwashi.model.*;
import com.miwashi.repositories.*;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.util.*;

@Configuration
@RestController
public class GroupController {

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
    

    @RequestMapping(value = "/api/groups/{groups}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/groups/{groups}", notes = "Returns a status")
    public GroupsSummary getAllRequirementsNew(@PathParam(value = "groups") @PathVariable final String groups) {
    	GroupsSummary testSummary = getGroupStatus();
    	return testSummary;
    }
    
    @RequestMapping("/groups/{groups}")
    public ModelAndView getAllTeams(@PathParam(value = "groups") @PathVariable final String groups) {
        ModelAndView mav = new ModelAndView("groups");
        GroupsSummary testSummary = getGroupStatus();
    	mav.addObject("summary", testSummary);
    	return mav;
    }
    
    private GroupsSummary getGroupStatus(){
    	GroupsSummary testSummary = new GroupsSummary();
    	
    	Iterable<Group> groupsIter = groupRepository.findAll();
    	groupsIter.forEach( group -> {
    		testSummary.add(group);
        });
    	
    	for(SimpleGroup group : testSummary.getGroups()){
    		Iterable<Requirement> requirementIter = requirementRepository.findBySubGroupId(group.getId());
    		requirementIter.forEach( requirement -> {
    			group.add(requirement);
            });
    	}
    	testSummary.clean();
        return testSummary;
    }

    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getTeamn(@PathParam(value = "Path id") @PathVariable final Long id) {
        ModelAndView mav = new ModelAndView("group");
        GroupSummary groupSummary = getGroupSummary(id);
        mav.addObject("summary",groupSummary);
        return mav;
    }
    
    @RequestMapping(value = "/api/group/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/groupn/{id}", notes = "Returns a status")
    public GroupSummary getTeamForApin(@PathParam(value = "Path id") @PathVariable final Long id) {
    	GroupSummary groupSummary = getGroupSummary(id);
        return groupSummary;
    }
    
    GroupSummary getGroupSummary(long id){
    	GroupSummary summary = new GroupSummary();
    	Iterable<Group> groupIter =  groupRepository.findById(id);
    	if(groupIter.iterator().hasNext()){
    		Group group = groupIter.iterator().next();
    		summary.add(group);
    	}
    	
    	Iterable<Requirement> requirementIter = requirementRepository.findBySubGroupId(id);
    	for(Requirement requirement : requirementIter){
    		summary.add(requirement);
    	}
    	return summary;
    }
}
