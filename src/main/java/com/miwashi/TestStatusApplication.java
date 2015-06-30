package com.miwashi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ConfigurationProperties
public class TestStatusApplication {


    @Value("${configuration.projectName}")
    void setProjectName(String projectName){
        System.out.println("Setting name to " + projectName);
    }

    @Autowired
    void setEnvrironment(Environment env){
        System.out.println("Setting environment: " + env.getProperty("configuration.projectName"));
    }


    public static void main(String[] args) {
        SpringApplication.run(TestStatusApplication.class, args);
    }
}
