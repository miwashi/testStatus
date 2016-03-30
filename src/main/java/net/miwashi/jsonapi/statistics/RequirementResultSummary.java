package net.miwashi.jsonapi.statistics;

import java.util.Date;
import java.util.List;

import net.miwashi.jsonapi.SimpleTimeStamp;
import net.miwashi.teststatus.model.Result;

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
