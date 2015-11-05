package com.miwashi.model.transients.jsonapi;

import java.util.Date;
import java.util.List;

import com.miwashi.model.Result;

public class LastResult {
	
	SimpleTimeStamp when = new SimpleTimeStamp(null); 
	
	public LastResult(List<Result> results){
		Date lastResult = null;
		for(Result result : results){
			if(lastResult!=null){
				if(( (result.getStartTime()!=null)) && (lastResult.getTime() < result.getStartTime().getTime())){
					lastResult = result.getStartTime();
				}
			}else{
				lastResult = result.getStartTime();
			}
		}
		when = new SimpleTimeStamp(lastResult);
	}
	
	public SimpleTimeStamp getWhen(){
		return when;
	}
}
