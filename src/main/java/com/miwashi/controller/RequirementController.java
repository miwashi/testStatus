package com.miwashi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.miwashi.model.Result;
import com.miwashi.model.transients.jsonapi.SimpleLink;
import com.miwashi.model.transients.jsonapi.SummaryRequirement;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.JobRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
    
    @Autowired
    JobRepository jobRepository;

@RequestMapping(value = "/api/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/requirement/{id}", notes = "Returns a status")
    public Map<String, Object>  getRequirement(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
    	Map<String, Object> result = new HashMap<String,Object>();
    	Requirement requirement = findRequirement(id);
        result.put("summary", new SummaryRequirement(requirement));
    	return result;
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
	public ModelAndView getRequirementById(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
		ModelAndView mav = new ModelAndView("requirement");
		Requirement requirement = findRequirement(id);
		mav.addObject("summary", new SummaryRequirement(requirement));
		
		Map<String, SimpleLink> links = new HashMap<String, SimpleLink>();
        links.put("team", new SimpleLink(requirement.getGroup().getId(), requirement.getGroup().getName()));
        links.put("group", new SimpleLink(requirement.getSubGroup().getId(), requirement.getSubGroup().getName()));
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);
        
	    return mav;
	}
    
    
    
    @RequestMapping(value = "/requirement/key/{key}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getRequirementById(@PathParam(value = "Path key") @PathVariable final String key) {
    	ModelAndView mav = new ModelAndView("requirement");
    	
    	Requirement requirement = findRequirement(key);
    	mav.addObject("title", "Requirement!");
    	mav.addObject("header", "Requirement!");
        mav.addObject("requirement", requirement);
        
        Map<String, SimpleLink> links = new HashMap<String, SimpleLink>();
        links.put("team", new SimpleLink(requirement.getGroup().getId(), requirement.getGroup().getName()));
        links.put("group", new SimpleLink(requirement.getGroup().getId(), requirement.getGroup().getName()));
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);
        
        return mav;
        
    }
    
    private Requirement findRequirement(long id){
    	Requirement requirement = null;
        Iterable<Requirement> requirementIter = requirementRepository.findById(id);
        if(requirementIter.iterator().hasNext()){
        	requirement = requirementIter.iterator().next();
        }
        
        if(requirement!=null){
	        List<Result> results = requirement.getResults();
	    	Collections.sort(results, new Comparator<Result>() {
	            @Override
	            public int compare(Result result1, Result result2) {
	                if (result1 == null || result2 == null || result1.getStartTime() == null || result2.getStartTime() == null) {
	                    return 0;
	                }
	                return result2.getStartTime().compareTo(result1.getStartTime());
	            }
	        });
        }
        return requirement;
    }
    
    private Requirement findRequirement(String key){
    	Requirement requirement = null;
        Iterable<Requirement> requirementIter = requirementRepository.findByKey(key);
        if(requirementIter.iterator().hasNext()){
        	requirement = requirementIter.iterator().next();
        }
        return requirement;
    }
    
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
      }
    
    private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
}
