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
import java.util.*;

@Configuration
@RestController
public class JobController {

    @Autowired
    JobRepository jobRepository;
    

    @RequestMapping(value = "/api/job/all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "/api/job/all", notes = "Returns a status")
    public List<Job> getAll() {
    	List<Job> jobs = new ArrayList<Job>();
        Iterable<Job> groups = jobRepository.findAll();
        if(groups.iterator().hasNext()){
            jobs.add(groups.iterator().next());
        }
        return jobs;
    }

    @RequestMapping("/job/all")
    public ModelAndView getAllTeams() {
        ModelAndView mav = new ModelAndView("jobs");
        
        List<Job> jobs = new ArrayList<Job>();
        Iterable<Job> groups = jobRepository.findAll();
        if(groups.iterator().hasNext()){
            jobs.add(groups.iterator().next());
        }
        
        mav.addObject("jobs", jobs);
        
        return mav;
    }
}
