package net.miwashi.teststatus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.ApiOperation;

import net.miwashi.jsonapi.StatisticsOverview;
import net.miwashi.teststatus.TestStatusApplication;

@CrossOrigin
@RestController
public class MainPageController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("stats", TestStatusApplication.getStats());
        mav.addObject("summary", new StatisticsOverview());
        return mav;
    }
    
    @RequestMapping(value = "/api/stats", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/stats", notes = "Returns a status")
    public Map<String, Object> getStats() {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("stats", TestStatusApplication.getStats());
    	result.put("summary", new StatisticsOverview());
        return result;
    }

}
