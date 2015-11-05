package com.miwashi.model.transients.jsonapi;

import java.util.Date;
import java.util.List;

import com.miwashi.model.Result;

public class RequirementResultSummary extends RequirementTestSummary {
	
	
	public RequirementResultSummary(List<Result> persistentResults) {
		super(persistentResults);
	}

	public boolean isUnstable(){
		return ((this.passes>0) && (this.fails > 0));
	}
	
	public SimpleTimeStamp getLastRun(){
		return new SimpleTimeStamp(new Date());
	}
	
	public SimpleTimeStamp getLastFail(){
		return new SimpleTimeStamp(new Date());
	}
	
	public SimpleTimeStamp getLastPass(){
		return new SimpleTimeStamp(new Date());
	}
	
	public SimpleTimeStamp getFirstRun(){
		return new SimpleTimeStamp(new Date());
	}
}
