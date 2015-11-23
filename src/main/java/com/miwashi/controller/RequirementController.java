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

import com.miwashi.jsonapi.SimpleGroup;
import com.miwashi.jsonapi.SimpleLink;
import com.miwashi.jsonapi.SimplePackageWithRequirements;
import com.miwashi.jsonapi.summaries.RequirementSummary;
import com.miwashi.model.JenkinsResult;
import com.miwashi.model.Job;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
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
    public Map<String, Object>  getRequirementByIdForAPI(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
    	Map<String, Object> result = new HashMap<String,Object>();
    	Requirement requirement = findRequirement(id);
        result.put("summary", new RequirementSummary(requirement));
    	return result;
    }
    
    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ModelAndView getRequirementById(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
		ModelAndView mav = new ModelAndView("requirement");
		Requirement requirement = findRequirement(id);
		mav.addObject("summary", new RequirementSummary(requirement));		
		Map<String, SimpleLink> links = new HashMap<String, SimpleLink>();
        return mav;
	}
    
    @RequestMapping(value = "/api/requirements/{selection}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/requirements/{selection}", notes = "Returns a status")
    public Map<String, Object>  getRequirementsForAPI(@PathParam(value = "Selection") @PathVariable final String selection) {
    	Map<String, Object> result = new HashMap<String,Object>();
    	
    	SimplePackageWithRequirements group = new SimplePackageWithRequirements(0, selection);
    	Iterable<Requirement> requirementIter = requirementRepository.findAll();
        for(Requirement requirement : requirementIter){
        	group.add(requirement);
        }
        group.clean();
        result.put("summary", group);
        return result;
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
