package com.miwashi.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.miwashi")
@EnableScheduling
@ConfigurationProperties("configuration")
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    private static final String API_VERSION = "v1";
    public static final SimpleDateFormat TIME_STAMP = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String getVersionedEndpointURL(String endpoint) {
        return "/" + API_VERSION + endpoint;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/api").setViewName("swagger");
        registry.addViewController("/thyme").setViewName("thyme");
        registry.addViewController("/settings").setViewName("settings");
        registry.addViewController("/").setViewName("index");
    }

}
