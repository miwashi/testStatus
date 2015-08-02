package com.miwashi;

import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
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
                    return result1.getStartTime().compareTo(result2.getStartTime());
                }
            });
            int indX = 0;
            for(Result result : results){
                if(indX++ > storedResults){
                    if(do_log_deletions) {
                        log.info("DELETING - as too many: " + result.getId());
                    }
                    resultRepository.delete(result);
                }
            }
        });
    }
}
