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
    public List<Job> getAllJobs() {
    	return findJobs();
    }
    
    @RequestMapping(value = "/api/job/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Job getJob(@PathParam(value = "Path id") @PathVariable final Long id) {
        return findJobsById(id);
    }

    @RequestMapping("/job/all")
    public ModelAndView getAllJobsAsMav() {
        ModelAndView mav = new ModelAndView("jobs");
        mav.addObject("jobs", findJobs());
        return mav;
    }
    
    @RequestMapping(value = "/job/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView getJobAsMav(@PathParam(value = "Path id") @PathVariable final Long id) {
        ModelAndView mav = new ModelAndView("job");
        mav.addObject("job",findJobsById(id));
        return mav;
    }
    
    private Job findJobsById(long id){
    	Job job = null;
        Iterable<Job> persitentJobs = jobRepository.findById(id);
        if(persitentJobs.iterator().hasNext()){
        	job = persitentJobs.iterator().next();
        }
        return job;
    }
    
    private List<Job> findJobs(){
    	List<Job> jobs = new ArrayList<Job>();
        Iterable<Job> persitentJobs = jobRepository.findAll();
        for(Job job : persitentJobs){
        	jobs.add(job);
        }
        Collections.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                if (job1 == null || job2 == null || job1.getTimeStamp() == null || job2.getTimeStamp() == null) {
                    return 0;
                }
                return job2.getTimeStamp().compareTo(job1.getTimeStamp());
            }
        });
        
        return jobs;
    }
}
