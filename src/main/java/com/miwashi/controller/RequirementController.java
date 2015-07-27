package com.miwashi.controller;

import com.miwashi.model.Browser;
import com.miwashi.model.Platform;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RestController
public class RequirementController {

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

    @RequestMapping(value = "/api/requirement/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/requirement/all", notes = "Returns a status")
    public List<Requirement> getAllRequirements() {
        List<Requirement> result =  new ArrayList<Requirement>();

        Iterable<Requirement> allRequiements = requirementRepository.findAll();
        allRequiements.forEach( aRquirement -> {
            aRquirement.getResults().forEach(aResult ->{
                if(aResult.getStartTime()!=null) {
                    if (aRquirement.getLastResult() == null) {
                        aRquirement.setLastResult(aResult);
                    }
                    if (aRquirement.getLastResult().getStartTime().getTime() > aResult.getStartTime().getTime()) {
                        aRquirement.setLastResult(aResult);
                    }
                }
            });

            result.add(aRquirement);
        });

        return result;
    }

    @RequestMapping(value = "/api/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/requirement/{id}", notes = "Returns a status")
    public Requirement getRequirement(@PathParam(value = "Path id") @PathVariable final Long id) {
    	Requirement requirement = null;
        Iterable<Requirement> requirementIter = requirementRepository.findById(id);
        if(requirementIter.iterator().hasNext()){
        	requirement = requirementIter.iterator().next();
        }

        return requirement;
    }

    @RequestMapping(value = "/api/requirement", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/requirement", notes = "Returns a status", response = Requirement.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok")
            ,@ApiResponse(code = 404, message = "missing")
    })
    public Requirement getRequirement(@ApiParam(value = "Parameter id") @RequestParam(required = false, defaultValue = "0") final String name) {
        System.out.println("Got " + name + " from id param!");
        return new Requirement("anyrec");
    }
    
    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getRequirementById(@PathParam(value = "Path id") @PathVariable final Long id) {
    	ModelAndView mav = new ModelAndView("requirement");
    	
    	Requirement requirement = null;
        Iterable<Requirement> requirementIter = requirementRepository.findById(id);
        if(requirementIter.iterator().hasNext()){
        	requirement = requirementIter.iterator().next();
        }
        mav.addObject("requirement", requirement);
        return mav;
        
    }
}
