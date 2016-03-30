package net.miwashi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by miwa01 on 15-06-29.
 */
@Component
@ConfigurationProperties("configuration")
public class ConfigurationProjectProperties {
    private String projectName;

    @Value("${configuration.jenkins.apisuffix:/api/json}")
    private String jenkinsApisuffux = "";

	@Value("${configuration.jenkins.testreportsuffix:testReport}")
    private String jenkinsTestreportsuffix = "";
	
	@Value("${configuration.jenkins.lastreportsuffix:lastBuild}")
    private String jenkinsLastbuildsuffix = "";

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

	public String getJenkinsApisuffux() {
		return jenkinsApisuffux;
	}

	public void setJenkinsApisuffux(String jenkinsApisuffux) {
		this.jenkinsApisuffux = jenkinsApisuffux;
	}

	public String getJenkinsTestreportsuffix() {
		return jenkinsTestreportsuffix;
	}

	public void setJenkinsTestreportsuffix(String jenkinsTestreportsuffix) {
		this.jenkinsTestreportsuffix = jenkinsTestreportsuffix;
	}

	public String getJenkinsLastbuildsuffix() {
		return jenkinsLastbuildsuffix;
	}

	public void setJenkinsLastbuildsuffix(String jenkinsLastbuildsuffix) {
		this.jenkinsLastbuildsuffix = jenkinsLastbuildsuffix;
	}
    
    
}
