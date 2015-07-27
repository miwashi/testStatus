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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    BrowserRepository browserRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    GroupRepository groupRepository;

    @RequestMapping(value = "/api/team/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/team/all", notes = "Returns a status")
    public List<Group> getAllRequirements() {
        List<Group> result =  new ArrayList<Group>();

        Iterable<Group> allGroups = groupRepository.findAll();
        allGroups.forEach(aGroup -> {
            result.add(aGroup);
        });

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
            group.setTested(group.getTested()+1);
            if(requirement.isSuccess()){
                group.setSuccesses(group.getSuccesses() +1);
            }
            if(requirement.isFailed()){
                group.setFails(group.getFails() +1);
            }
            if(!requirement.isTested()){

            }
            if(requirement.isUnstable()){
                group.setUnstable(group.getUnstable() +1);
            }
            group.add(requirement.getSubject());
            requirement.getSubject().add(requirement);
        }
        return group;
    }

    @RequestMapping("/teams")
    public ModelAndView getTeams() {
        ModelAndView mav = new ModelAndView("teams");

        Collection<Group> groups = new ArrayList<Group>();
        Iterable<Group> groupsIter = groupRepository.findAll();
        groupsIter.forEach( group -> {
            groups.add(group);

            Iterable<Requirement> requirementsIter = null;
            if(group.getName().indexOf(".")>=0){
                requirementsIter = requirementRepository.findBySubGroupId(group.getId());
            }else{
                requirementsIter = requirementRepository.findByGroupId(group.getId());
            }
            requirementsIter.forEach( requirement -> {
                group.setTested(group.getTested()+1);
                if(requirement.isSuccess()){
                    group.setSuccesses(group.getSuccesses() +1);
                }
                if(requirement.isFailed()){
                    group.setFails(group.getFails() +1);
                }
                if(!requirement.isTested()){

                }
                if(requirement.isUnstable()){
                    group.setUnstable(group.getUnstable() +1);
                }
            });

        });
        mav.addObject("teams", groups);

        return mav;
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
            group.setTested(group.getTested()+1);
            if(requirement.isSuccess()){
                group.setSuccesses(group.getSuccesses() +1);
            }
            if(requirement.isFailed()){
                group.setFails(group.getFails() +1);
            }
            if(!requirement.isTested()){

            }
            if(requirement.isUnstable()){
                group.setUnstable(group.getUnstable() +1);
            }
            group.add(requirement.getSubject());
            requirement.getSubject().add(requirement);
        }

        mav.addObject("team", group);

        return mav;
    }
}
