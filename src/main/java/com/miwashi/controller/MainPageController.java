package com.miwashi.controller;

import com.miwashi.TestStatusApplication;
import com.miwashi.model.Group;
import com.miwashi.model.transients.jsonapi.SummaryStatistics;
import com.wordnik.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainPageController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("stats", TestStatusApplication.getStats());
        mav.addObject("summary", new SummaryStatistics());
        
        Map<String, Long> links = new HashMap<String, Long>();
        links.put("team", null);
        links.put("group", null);
        links.put("subject", null); 
        links.put("requirement", null);
        links.put("result", null);
        mav.addObject("links", links);
        
        return mav;
    }
    
    @RequestMapping(value = "/api/stats", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/stats", notes = "Returns a status")
    public Map<String, Object> getStats() {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("stats", TestStatusApplication.getStats());
    	result.put("summary", new SummaryStatistics());
        return result;
    }

}
