package net.miwashi.jsonapi;

import java.util.Date;

import net.miwashi.jsonapi.statistics.JenkinsStat;
import net.miwashi.teststatus.model.Job;
import net.miwashi.teststatus.model.TestState;

public class SimpleJob {
	
	private long id; 
	private TestState status;
	private SimpleTimeStamp when;
	private long duration;
	private String browser;
	private String platform;
	private String job;
	private String buildUrl;
	
	private JenkinsStat jenkinsStat;
	
	public SimpleJob(Job peristentJob){
		jenkinsStat = new JenkinsStat(
				peristentJob.getJenkinsPassCount(), 
				peristentJob.getJenkinsFailCount(), 
				peristentJob.getJenkinsSkipCount(), 
				peristentJob.getJenkinsTotalCount()
				);
		duration = peristentJob.getJenkinsDuration();
		
		
		this.id = peristentJob.getId();
		browser = peristentJob.getBrowser();
		peristentJob.getBuildNumber();
		peristentJob.getBuildTag();
		job = peristentJob.getBuildUrl();
		buildUrl = peristentJob.getBuildUrl();
		peristentJob.getCheckins();
		peristentJob.getGitBranch();
		peristentJob.getGitCommit();
		peristentJob.getGitURL();
		peristentJob.getGrid();
		peristentJob.getHost();
		
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
	
	public JenkinsStat getJenkinsStat(){
		return jenkinsStat;
	}
}
