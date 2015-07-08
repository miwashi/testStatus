package com.miwashi.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.miwashi")
@EnableScheduling
@SuppressWarnings("unused")
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    private static final String API_VERSION = "v1";

    public static String getVersionedEndpointURL(String endpoint) {
        return "/" + API_VERSION + endpoint;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/thyme").setViewName("thyme");
    }

}
