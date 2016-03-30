package net.miwashi.teststatus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.miwashi.receiver.UDPClient;

import java.util.Map;

@Service
@ConditionalOnProperty("configuration.testdata")
public class TestDataService {

    @Scheduled(fixedRateString = "${configuration.schedule.testresult.rate}", initialDelayString = "${configuration.schedule.testresult.delay}")
    public void sendTestResult(){
        Map<String, String> data = UDPClient.getRandomTest();
        UDPClient udpClient = new UDPClient();
        udpClient.beforeTest(data);
        udpClient.afterTest(data);
    }
}
