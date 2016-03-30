package net.miwashi.teststatus.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.ApiOperation;

import net.miwashi.jsonapi.StatisticsOverview;
import net.miwashi.jsonapi.URLRequirement;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.repositories.BrowserRepository;
import net.miwashi.teststatus.repositories.JobRepository;
import net.miwashi.teststatus.repositories.PlatformRepository;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.ResultRepository;

@CrossOrigin
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
    public  Map<String, Object> getStatisticsOverviewForAPI() {
    	Map<String, Object> response = new HashMap<String,Object>();
    	
    	StatisticsOverview overview = createOverview();
    	
    	//ParameterizedRequirementSummary summary = getSummary();
        response.put("overview", overview);
        
        return response;
    }
    
    @RequestMapping(value = "/api/statistics/requirements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/statistics/requirements", notes = "Returns a status")
    public  URLRequirement[] getStatisticsRequirementsForAPI(@RequestParam(value="id", required=false) String _) {
    	Collection<URLRequirement> response = new ArrayList<URLRequirement>();
    	StatisticsOverview overview = createOverview();
    	return overview.getRequirements().toArray(new URLRequirement[overview.getRequirements().size()]);
    	//return new URLRequirement[]{};
    }
    
    @RequestMapping(value = "/api/statistics/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/statistics/requirement/{id}", notes = "Returns a status")
    public  URLRequirement getStatisticsRequirementForAPI(@PathParam(value = "Path id") @PathVariable final Long id) {
    	StatisticsOverview overview = createOverview();
    	return (URLRequirement) overview.getRequirements().iterator().next();
    }
    
    @RequestMapping(value = "/statistics/requirement/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/statistics/requirement/{id}", notes = "Returns a status")
    public  ModelAndView getStatisticsRequirement(@PathParam(value = "Path id") @PathVariable final Long id) {
    	ModelAndView mav = new ModelAndView("statistics_requirement");
    	StatisticsOverview overview = createOverview();
    	mav.addObject("requirement", (URLRequirement) overview.getRequirements().iterator().next());
    	return mav;
    }
    
    @RequestMapping(value = "/statistics/result/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/statistics/result/{id}", notes = "Returns a status")
    public  ModelAndView getStatisticsResult(@PathParam(value = "Path id") @PathVariable final Long id) {
    	ModelAndView mav = new ModelAndView("statistics_result");
    	StatisticsOverview overview = createOverview();
    	mav.addObject("result", (URLRequirement) overview.getRequirements().iterator().next());
    	return mav;
    }
    
    @RequestMapping(value = "/statistics/all", method = RequestMethod.GET)
    public ModelAndView StatisticsOverviewForNewPage() {
    	ModelAndView mav = new ModelAndView("statistics");
    	StatisticsOverview overview = createOverview();
        mav.addObject("overview", overview);
        return mav;
    }
    
    private StatisticsOverview createOverview(){
    	StatisticsOverview overview = new StatisticsOverview();
        Iterable<Requirement> requirementIter = requirementRepository.findAll();
        requirementIter.forEach(requirement -> {
        	if(requirement.isStatisticsRequirement()){
        		overview.add(requirement);
        	}else{
        		//summary.add(requirement);
        	}
        });
        return overview;
    }

}
