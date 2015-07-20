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

    @RequestMapping(value = "/requirement/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement/all", notes = "Returns a status")
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

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement/{id}", notes = "Returns a status")
    public Requirement getRequirement(@PathParam(value = "Path id") @PathVariable final Long id) {
        requirementRepository.findById(id);


        return new Requirement("anyrec");
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement", notes = "Returns a status", response = Requirement.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok")
            ,@ApiResponse(code = 404, message = "missing")
    })
    public Requirement getRequirement(@ApiParam(value = "Parameter id") @RequestParam(required = false, defaultValue = "0") final String name) {
        System.out.println("Got " + name + " from id param!");
        return new Requirement("anyrec");
    }

    @RequestMapping(value = "/requirement/register", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/requirement/register", notes = "Returns a status")
    public void registerResult(
            @ApiParam(value = "Class and Method") @RequestParam(required = true, defaultValue = "") final String name
            ,@ApiParam(value = "Result of test of requriement") @RequestParam(required = false, defaultValue = "false") final Boolean status
            ,@ApiParam(value = "Firefox, IE, Opera, Chrome") @RequestParam(required = false, defaultValue = "firefox") String browser
            ,@ApiParam(value = "Mac,Windows,Linux") @RequestParam(required = false, defaultValue = "linux") String platform
        ){
        Browser aBrowser = loadBrowser(browser);
        Platform aPlatform = loadPlatform(platform);

        Requirement requirement = new Requirement(name);
        Iterable<Requirement> requirements = requirementRepository.findByName(name);
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
