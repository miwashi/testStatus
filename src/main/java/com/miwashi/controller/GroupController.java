package com.miwashi.controller;

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
    

    @RequestMapping(value = "/api/team/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/team/all", notes = "Returns a status")
    public Map<String, Object> getAllRequirements() {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	List<Group> groups = findGroups();
    	Set<String> topGroups = findTopGroups();
        
        result.put("groups", groups);
        result.put("topteams", topGroups);
        
        return result;
    }

    @RequestMapping(value = "/api/team/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/team/{id}", notes = "Returns a status")
    public Group getGroup(@PathParam(value = "Path id") @PathVariable final Long id) {
        Group group = new Group();
        Iterable<Group> groups = groupRepository.findById(id);
        if(groups.iterator().hasNext()){
            group = groups.iterator().next();
        }

        Iterable<Requirement> requirementsIter = null;
        if(group.getName().indexOf(".")>=0){
            requirementsIter = requirementRepository.findBySubGroupId(group.getId());
        }else{
            requirementsIter = requirementRepository.findByGroupId(group.getId());
        }
        for(Requirement requirement : requirementsIter){
        	group.addStat(requirement);
            group.add(requirement.getSubject());
            requirement.getSubject().add(requirement);
        }
        
        return group;
    }

    @RequestMapping("/team/all")
    public ModelAndView getAllTeams() {
        ModelAndView mav = new ModelAndView("teams");
        
        
    	List<Group> groups = findGroups();
    	Set<String> topGroups = findTopGroups();
    	
        mav.addObject("teams", groups);
        mav.addObject("topteams", topGroups);
        return mav;
    }
    
    private Set<String> findTopGroups(){
    	Set<String> topGroups = new HashSet<String>();
    	Iterable<Group> groupsIter = groupRepository.findAll();
    	groupsIter.forEach( group -> {
            group.resetStat();

            //Find requirements for every group
            Iterable<Requirement> requirementsIter = null;
            if(group.getName().indexOf(".")>=0){
                requirementsIter = requirementRepository.findBySubGroupId(group.getId());
            }else{
                requirementsIter = requirementRepository.findByGroupId(group.getId());
                topGroups.add(group.getName());
            }
            
            //For every requirement belonging to a group, calculate group stats.
            requirementsIter.forEach( requirement -> {
            	group.addStat(requirement);
            });
        });
    	return topGroups;
    }
    
    private List<Group> findGroups(){
    	List<Group> groups = new ArrayList<Group>();
        
        //Find all groups
        Iterable<Group> groupsIter = groupRepository.findAll();
        groupsIter.forEach( group -> {
            groups.add(group);
            group.resetStat();

            //Find requirements for every group
            Iterable<Requirement> requirementsIter = null;
            if(group.getName().indexOf(".")>=0){
                requirementsIter = requirementRepository.findBySubGroupId(group.getId());
            }else{
                requirementsIter = requirementRepository.findByGroupId(group.getId());
            }
            
            //For every requirement belonging to a group, calculate group stats.
            requirementsIter.forEach( requirement -> {
            	group.addStat(requirement);
            });
        });

        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group group1, Group group2) {
                if (group1 == null || group2 == null || group1.getName() == null || group2.getName() == null) {
                    return 0;
                }
                return group1.getName().compareTo(group2.getName());
            }
        });
        return groups;
    }
    
    @RequestMapping(value = "/team/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getTeam(@PathParam(value = "Path id") @PathVariable final Long id) {
        ModelAndView mav = new ModelAndView("team");

        Group group = new Group();
        Iterable<Group> groups = groupRepository.findById(id);
        if(groups.iterator().hasNext()){
            group = groups.iterator().next();
        }

        Iterable<Requirement> requirementsIter = null;
        if(group.getName().indexOf(".")>=0){
            requirementsIter = requirementRepository.findBySubGroupId(group.getId());
        }else{
            requirementsIter = requirementRepository.findByGroupId(group.getId());
        }
        for(Requirement requirement : requirementsIter){
        	group.addStat(requirement);
            group.add(requirement.getSubject());
            requirement.getSubject().add(requirement);
        }

        List<Subject> unstable = new ArrayList<Subject>();
        List<Subject> faulty = new ArrayList<Subject>();
        List<Subject> ok = new ArrayList<Subject>();
        for(Subject aSubject : group.getSubjects()){
        	if(aSubject.isFailed()){
        		faulty.add(aSubject);
        	}	
        	
        	if(aSubject.isUnstable()){
        		unstable.add(aSubject);
        	}
        	
        	if(aSubject.isSucceeded()){
        		ok.add(aSubject);
        	}
        }

        Collections.sort(faulty, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2) {
                if (subject1 == null || subject2 == null || subject1.getName() == null || subject2.getName() == null) {
                    return 0;
                }
                return subject1.getName().compareTo(subject2.getName());
            }
        });
        
        Collections.sort(unstable, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2) {
                if (subject1 == null || subject2 == null || subject1.getName() == null || subject2.getName() == null) {
                    return 0;
                }
                return subject1.getName().compareTo(subject2.getName());
            }
        });
        
        Collections.sort(ok, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2) {
                if (subject1 == null || subject2 == null || subject1.getName() == null || subject2.getName() == null) {
                    return 0;
                }
                return subject1.getName().compareTo(subject2.getName());
            }
        });
        
        mav.addObject("failed", faulty);
        mav.addObject("unstable", unstable);
        mav.addObject("ok", ok);
        mav.addObject("team", group);
        
        return mav;
    }
}
