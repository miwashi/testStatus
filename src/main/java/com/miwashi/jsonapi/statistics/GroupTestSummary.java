package com.miwashi.jsonapi.statistics;

import java.util.List;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class GroupTestSummary extends RequirementTestSummary{
	
	int unstable = 0;
	
	public GroupTestSummary(){
	}
	
	public GroupTestSummary(List<Result> results){
		this.add(results);
	}
	
	public int getUnstable() {
		return unstable;
	}
	
	public int getUnstableRatio() {
		return  (getRuns()==0?0:Math.round((100 * (getRuns()-getPasses())) / getRuns()));
	}
	
	public void addUnstable(int unstable) {
		this.unstable=unstable;
	}
		
	@Override
	public void add(RequirementTestSummary summary){
		super.add(summary);
		if(summary.isUnstable()){
			unstable++;
		}
	}
	
	@Override
	public void merge(RequirementTestSummary summary){
		super.merge(summary);
		if(summary.isUnstable()){
			unstable++;
		}
	}
}
