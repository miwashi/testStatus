package com.miwashi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.miwashi.model.JenkinsResult;
import com.miwashi.model.Job;
import com.miwashi.model.Requirement;
import com.miwashi.model.transients.jsonapi.ParameterizedRequirementSummary;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.JobRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Configuration
@RestController
public class StatisticsController {
	
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
    JobRepository jobRepository;

    @RequestMapping(value = "/api/statistics/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/statistics/all", notes = "Returns a status")
    public  Map<String, Object> getAllRequirements() {
    	Map<String, Object> response = new HashMap<String,Object>();
    	ParameterizedRequirementSummary summary = getSummary();
        response.put("statistics", summary);
        return response;
    }
    
    @RequestMapping(value = "/statistics/all", method = RequestMethod.GET)
    public ModelAndView getRequirementById() {
    	ModelAndView mav = new ModelAndView("statistics");
    	
    	ParameterizedRequirementSummary summary = getSummary();
        
        mav.addObject("summary", summary);
        
        Map<String, Long> links = new HashMap<String, Long>();
        links.put("team", null);
        links.put("group", null);
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);
        
        return mav;
        
    }
    
    private ParameterizedRequirementSummary getSummary(){
    	ParameterizedRequirementSummary summary = new ParameterizedRequirementSummary();
        Iterable<Requirement> requirementIter = requirementRepository.findAll();
        requirementIter.forEach(requirement -> {
        	if(requirement.isStatisticsRequirement()){
        		summary.add(requirement);
        	}else{
        		//summary.add(requirement);
        	}
        });
        return summary;
    }

}
