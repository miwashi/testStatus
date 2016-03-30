package net.miwashi.jsonapi;

import net.miwashi.teststatus.model.Result;
import net.miwashi.teststatus.model.TestState;

public class SimpleResult {
	
	private long id; 
	private TestState status;
	private SimpleTimeStamp when;
	private long duration;
	private String browser;
	private String platform;
	private String job;
	private String buildUrl;
	
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
	
	public SimpleResult(Result persistentResult){
		this.id = persistentResult.getId();
		this.status = persistentResult.getStatus();
		this.when = new SimpleTimeStamp(persistentResult.getStartTime());
		this.duration = persistentResult.getDuration();
		browser = persistentResult.getBrowser()==null?"":persistentResult.getBrowser().getName();
		platform = persistentResult.getPlatform()==null?"":persistentResult.getPlatform().getName();
		job = persistentResult.getJob()==null?"":persistentResult.getJob().getName();
		buildUrl = persistentResult.getJob()==null?"":persistentResult.getJob().getBuildUrl();
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
