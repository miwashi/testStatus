package com.miwashi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by miwa01 on 15-06-29.
 */
@Component
@ConfigurationProperties("configuration")
public class ConfigurationProjectProperties {
    private String projectName;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        System.out.println("Setting configuration to projectName: " + projectName);
        this.projectName = projectName;
    }
}
