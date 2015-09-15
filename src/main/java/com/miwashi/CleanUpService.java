package com.miwashi;

import com.miwashi.model.Fail;
import com.miwashi.model.Job;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.repositories.FailRepository;
import com.miwashi.repositories.JobRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class CleanUpService {

    private Log log = LogFactory.getLog(CleanUpService.class);

    @Value("${configuration.storagedays:5}")
    private int storageDays = 5;

    @Value("${configuration.storedresults:20}")
    private int storedResults = 20;

    @Value("${configuration.log.deletions:false}")
    private boolean do_log_deletions = false;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    RequirementRepository requirementRepository;
    
    @Autowired
    JobRepository jobRepository;
    
    @Autowired
    FailRepository failRepository;

    @Scheduled(fixedRateString = "${configuration.schedule.cleanup.rate}")
    public void cleanUp(){
        {
            Iterable<Result> resultIter = resultRepository.findAll();

            resultIter.forEach(result -> {
                DateTime cleanUpTime = DateTime.now().minusDays(storageDays);
                DateTime thisTime = new DateTime(result.getStartTime());
                if (thisTime.isBefore(cleanUpTime)) {
                    if(do_log_deletions) {
                        log.info("DELETING - as old: " + result.getId());
                    }
                    resultRepository.delete(result);
                    Iterable<Fail> failIter = failRepository.findByResultId(result.getId());
                	if(failIter.iterator().hasNext()){
                		failRepository.delete(failIter.iterator().next());
                	}
                }
            });
        }
        
        Iterable<Requirement> requirementIter = requirementRepository.findAll();
        requirementIter.forEach(requirement -> {
            List<Result> results = requirement.getResults();
            Collections.sort(results, new Comparator<Result>() {

                @Override
                public int compare(Result result1, Result result2) {
                    if(result1==null || result2==null || result1.getStartTime()==null || result2.getStartTime()== null){
                        return 0;
                    }
                    return result2.getStartTime().compareTo(result1.getStartTime());
                }
            });
            int indX = 0;
            for(Result result : results){
                if(indX++ > storedResults){
                    if(do_log_deletions) {
                        log.info("DELETING - as too many: " + result.getId());
                    }
                    resultRepository.delete(result);
                    Iterable<Fail> failIter = failRepository.findByResultId(result.getId());
                	if(failIter.iterator().hasNext()){
                		failRepository.delete(failIter.iterator().next());
                	}
                }
            }
        });
    }
    
    @Scheduled(fixedRateString = "${configuration.schedule.statcalc.rate}")
    public void calculateStats(){
    	int numberOfRequriements = 0;
    	int numberOfVerifiedRequirements = 0;
    	int numberOfUnstableRequirements = 0;
    	int numberOfFailedRequirements = 0;
    	int numberOfTestedRequiements = 0;
    	
    	
    	Iterable<Requirement> requirements = requirementRepository.findAll();
    	for(Requirement requirement : requirements){
    		numberOfRequriements++;
    		if(requirement.isFailed()){
    			numberOfFailedRequirements++;
    		}
    		if (requirement.isSuccess()){
    			numberOfVerifiedRequirements++;
    		}
    		if(requirement.isUnstable()){
    			numberOfUnstableRequirements++;
    		}
    		if(requirement.isTested()){
    			numberOfTestedRequiements++;
    		}
    		
    		TestStatusApplication.getStats().setNumberOfFailedRequirements(numberOfFailedRequirements);
        	TestStatusApplication.getStats().setNumberOfRequirements(numberOfRequriements);
        	TestStatusApplication.getStats().setNumberOfTestedRequirements(numberOfVerifiedRequirements + numberOfFailedRequirements);
        	TestStatusApplication.getStats().setNumberOfUnstableRequirements(numberOfUnstableRequirements);
        	TestStatusApplication.getStats().setNumberOfVerifiedRequirements(numberOfVerifiedRequirements);
    	}
    	
    	Iterable<Job> jobs = jobRepository.findAll();
    	for(Job job : jobs){
    		TestStatusApplication.getStats().getJenkises().add(job.getJenkinsUrl());
    		TestStatusApplication.getStats().getJobs().add(job.getName());
    		TestStatusApplication.getStats().getBrowsers().add(job.getBrowser());
    		TestStatusApplication.getStats().getPlatforms().add(job.getPlatform());
    	}
    	TestStatusApplication.getStats().setNumberOfBrowsers(TestStatusApplication.getStats().getBrowsers().size());
		TestStatusApplication.getStats().setNumberOfJenkises(TestStatusApplication.getStats().getJenkises().size());
		TestStatusApplication.getStats().setNumberOfJobs(TestStatusApplication.getStats().getJobs().size());
		TestStatusApplication.getStats().setNumberOfPlatforms(TestStatusApplication.getStats().getPlatforms().size());
		TestStatusApplication.getStats().setNumberOfTeams(TestStatusApplication.getStats().getTeams().size());
    }
}
