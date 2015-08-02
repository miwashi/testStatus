package com.miwashi;

import com.miwashi.model.Result;
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

import java.util.Map;

@Service
public class CleanUpService {

    private Log log = LogFactory.getLog(CleanUpService.class);

    @Value("${configuration.storagedays:5}")
    private int storageDays = 5;

    @Value("${configuration.log.deletions:false}")
    private boolean do_log_recievied_results = false;

    @Autowired
    ResultRepository resultRepository;

    @Scheduled(fixedRateString = "${configuration.schedule.cleanup.rate}")
    public void cleanUp(){
        Iterable<Result> resultIter = resultRepository.findAll();
        System.out.println("******************************************");
        resultIter.forEach(result -> {
            DateTime cleanUpTime = DateTime.now().minusDays(storageDays);
            System.out.print("\t" + result.getStartTime());
            DateTime thisTime = new DateTime(result.getStartTime());
            if(thisTime.isBefore(cleanUpTime)){
                log.info("DELETING - " + result.getId());
                resultRepository.delete(result);
            }
        });
        System.out.println();
    }
}
