package com.miwashi.controller;

import com.miwashi.model.*;
import com.miwashi.model.transients.jsonapi.SimpleGroup;
import com.miwashi.model.transients.jsonapi.SimplePackageWithRequirements;
import com.miwashi.model.transients.jsonapi.SimpleTeam;
import com.miwashi.model.transients.jsonapi.SummaryGroup;
import com.miwashi.model.transients.jsonapi.SummaryGroups;
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
    public SummaryGroups getAllRequirementsNew(@PathParam(value = "groups") @PathVariable final String groups) {
    	SummaryGroups testSummary = getGroupStatus();
    	return testSummary;
    }
    
    @RequestMapping("/groups/{groups}")
    public ModelAndView getAllTeams(@PathParam(value = "groups") @PathVariable final String groups) {
        ModelAndView mav = new ModelAndView("groups");
        SummaryGroups testSummary = getGroupStatus();
    	mav.addObject("summary", testSummary);
    	
    	Map<String, Long> links = new HashMap<String, Long>();
        links.put("team", null);
        links.put("group", null);
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);
        
        return mav;
    }
    
    private SummaryGroups getGroupStatus(){
    	SummaryGroups testSummary = new SummaryGroups();
    	
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
        SummaryGroup groupSummary = getGroupSummary(id);
        mav.addObject("summary",groupSummary);
        
        Map<String, Long> links = new HashMap<String, Long>();
        links.put("team", null);
        links.put("group", null);
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);

        return mav;
    }
    
    @RequestMapping(value = "/api/group/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/groupn/{id}", notes = "Returns a status")
    public SummaryGroup getTeamForApin(@PathParam(value = "Path id") @PathVariable final Long id) {
    	SummaryGroup groupSummary = getGroupSummary(id);
        return groupSummary;
    }
    
    SummaryGroup getGroupSummary(long id){
    	SummaryGroup summary = new SummaryGroup();
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
