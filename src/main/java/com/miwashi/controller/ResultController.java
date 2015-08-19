package com.miwashi.controller;

import com.miwashi.model.Browser;
import com.miwashi.model.JenkinsResult;
import com.miwashi.model.Job;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RestController
public class ResultController {

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
    
    
    @RequestMapping(value = "/api/result/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/result/{id}", notes = "Returns a result")
    public Map<String, Object>  getResultForAPIById(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
    	Map<String, Object> result = new HashMap<String,Object>();
    	
    	Result aResult = null;
    	Iterable<Result> resultIter = resultRepository.findById(id);
    	if(resultIter.iterator().hasNext()){
    		aResult = resultIter.iterator().next();
    	}
    	
    	Requirement requirement = null;
    	if(result!=null){
    		Iterable<Requirement> reqIter = requirementRepository.findById(aResult.getRequirementId());
    		if(reqIter.iterator().hasNext()){
    			requirement = reqIter.iterator().next();
        	}
    	}
    	result.put("requirement", requirement);
    	result.put("result", aResult);
    	
    	return result;
    }
    
    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getResultById(@PathParam(value = "Path id") @PathVariable final Long id, @ApiParam(value = "Build id") @RequestParam(required = false, defaultValue = "0") final String buildId) {
    	ModelAndView mav = new ModelAndView("result");
    	
    	Result result = null;
    	Iterable<Result> resultIter = resultRepository.findById(id);
    	if(resultIter.iterator().hasNext()){
    		result = resultIter.iterator().next();
    	}
    	Requirement requirement = null;
    	if(result!=null){
    		Iterable<Requirement> reqIter = requirementRepository.findById(result.getRequirementId());
    		if(reqIter.iterator().hasNext()){
    			requirement = reqIter.iterator().next();
        	}
    	}
    	mav.addObject("requirement", requirement);
    	mav.addObject("result", result);
    	return mav;
    }
    
    

    @RequestMapping(value = "/api/result/register", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/result/register", notes = "Returns a status")
    public void registerResult(
            @ApiParam(value = "Class and Method") @RequestParam(required = true, defaultValue = "") final String key
            ,@ApiParam(value = "Result of test of requriement") @RequestParam(required = false, defaultValue = "false") final String status
            ,@ApiParam(value = "Firefox, IE, Opera, Chrome") @RequestParam(required = false, defaultValue = "firefox") String browser
            ,@ApiParam(value = "Mac,Windows,Linux") @RequestParam(required = false, defaultValue = "linux") String platform
        ){
        Browser aBrowser = loadBrowser(browser);
        Platform aPlatform = loadPlatform(platform);

        Requirement requirement = new Requirement(key);
        Iterable<Requirement> requirements = requirementRepository.findByKey(key);
        if(requirements.iterator().hasNext()){
            requirement = requirements.iterator().next();
        }

        requirementRepository.save(requirement);

        Result result = new Result(status);
        result.setBrowser(aBrowser);
        result.setPlatform(aPlatform);
        requirement.add(result);
        requirementRepository.save(requirement);
    }

    Browser loadBrowser(String browserName){
        if(browserName==null || browserName.isEmpty()){
            browserName = DEFAULT_BROWSER;
        }
        browserName = browserName.toLowerCase();

        Iterable<Browser> browsers = browserRepository.findByName(browserName);
        Browser aBrowser = new Browser(browserName);
        if(browsers.iterator().hasNext()){
            aBrowser = browsers.iterator().next();
        }else{
            browserRepository.save(aBrowser);
        }
        return aBrowser;
    }

    Platform loadPlatform(String platformName){
        if(platformName==null || platformName.isEmpty()){
            platformName = DEFAULT_PLATFORM;
        }
        platformName = platformName.toLowerCase();

        Iterable<Platform> platforms = platformRepository.findByName(platformName);
        Platform aPlatform = new Platform(platformName);
        if(platforms.iterator().hasNext()){
            aPlatform = platforms.iterator().next();
        }else{
            platformRepository.save(aPlatform);
        }
        return aPlatform;
    }
}
