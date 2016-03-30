package net.miwashi.jsonapi.statistics;

import java.util.List;

import net.miwashi.teststatus.model.Result;

public class RequirementStat extends AbstractStatistics{

	private boolean isUnstable;
	
	public RequirementStat(){
		super("requirement-statistics");
	}
	
	public RequirementStat(List<Result> results){
		super("requirement-statistics");
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
