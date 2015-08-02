package com.miwashi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
public class CleanUpService {

    @Scheduled(fixedRateString = "${configuration.schedule.cleanup.rate}", initialDelayString = "${configuration.schedule.cleanup.delay}")
    public void cleanUp(){
    }
}
