package com.miwashi.model.transients.jsonapi;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class SimpleResult {
	
	private long id; 
	private TestState status;
	private SimpleTimeStamp when;
	private long duration;
	
	public long getId(){
		return id;
	}
	
	public TestState getStatus() {
		return status;
	}

	public void setStatus(TestState status) {
		this.status = status;
	}
	
	public SimpleResult(Result result){
		this.id = result.getId();
		this.status = result.getStatus();
		this.when = new SimpleTimeStamp(result.getStartTime());
		this.duration = result.getDuration();
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
