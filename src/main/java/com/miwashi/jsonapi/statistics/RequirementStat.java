package com.miwashi.jsonapi.statistics;

import java.util.List;

import com.miwashi.model.Result;

public class RequirementStat {

	private boolean isUnstable;
	
	public RequirementStat(List<Result> results){
		int numFails = 0;
		int numSuccess = 0;
		
		for(Result result : results){
			numSuccess+=result.isSuccess()?1:0;
			numFails+=result.isFail()?1:0;
		}
		
		isUnstable = (numFails>0) && (numSuccess>0);
	}
	
	
	public boolean isUnstable(){
		return isUnstable();
	}
}
