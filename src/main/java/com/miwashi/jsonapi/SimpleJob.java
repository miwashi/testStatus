package com.miwashi.jsonapi;

import java.util.Date;

import com.miwashi.model.Job;
import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class SimpleJob {
	
	private long id; 
	private TestState status;
	private SimpleTimeStamp when;
	private long duration;
	private String browser;
	private String platform;
	private String job;
	private String buildUrl;
	
	public SimpleJob(Job peristentJob){
		this.id = peristentJob.getId();
		browser = peristentJob.getBrowser();
		peristentJob.getBuildNumber();
		peristentJob.getBuildTag();
		job = peristentJob.getBuildUrl();
		peristentJob.getCheckins();
		peristentJob.getGitBranch();
		peristentJob.getGitCommit();
		peristentJob.getGitURL();
		peristentJob.getGrid();
		peristentJob.getHost();
		peristentJob.getJenkinsDuration();
		peristentJob.getJenkinsFailCount();
		peristentJob.getJenkinsPassCount();
		peristentJob.getJenkinsPassCount();
		peristentJob.getJenkinsSkipCount();
		peristentJob.getJenkinsTotalCount();
		//job = peristentJob.getJenkinsUrl();
		//job = peristentJob.getJenkinstTestReportUrl();
		peristentJob.getJobStatusUrl();
		peristentJob.getKey();
		//job = peristentJob.getLatestResultReportUrl();
		peristentJob.getName();
		peristentJob.getNodeName();
		platform = peristentJob.getPlatform();
		Date timeStamp = peristentJob.getTimeStamp();
		if(timeStamp!=null){
			when = new SimpleTimeStamp(timeStamp);
		}
		peristentJob.getUser();
	}

	public String getBrowser() {
		return browser;
	}

	public String getPlatform() {
		return platform;
	}

	public String getJob() {
		return job;
	}
	
	public String getBuildUrl() {
		return buildUrl;
	}

	public long getId(){
		return id;
	}
	
	public TestState getStatus() {
		return status;
	}

	public void setStatus(TestState status) {
		this.status = status;
	}
	
	

	public SimpleTimeStamp getWhen() {
		return when;
	}

	public void setTime(SimpleTimeStamp when) {
		this.when = when;
	}

	public long getDuration(){
		return duration;
	}
}
