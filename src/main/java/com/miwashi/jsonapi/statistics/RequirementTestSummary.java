package com.miwashi.jsonapi.statistics;

import java.util.List;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class RequirementTestSummary {
	
	int total = 0;
	int passes = 0;
	int fails = 0;
	int skips = 0;
	
	public RequirementTestSummary(){
	}
	
	public RequirementTestSummary(List<Result> results){
		this.add(results);
	}
	
	public int getPasses() {
		return passes;
	}
	
	public int getPassRatio() {
		return 100 - (getRuns()==0?0:Math.round((100 * (getRuns()-getPasses())) / getRuns()));
	}
	
	public void addPasses(int passes) {
		total+=passes;
		this.passes+=passes;
	}
	
	public int getFails() {
		return fails;
	}
	
	public int getFailRatio() {
		return 100 - (getRuns()==0?0:Math.round((100 * (getRuns()-getFails())) / getRuns()));
	}
	
	public void addFails(int fails) {
		total+=fails;
		this.fails+=fails;
	}
	
	public int getSkips() {
		return skips;
	}
	
	public int getSkipRatio() {
		return 100 - (getTotal()==0?0:Math.round((100 * (getTotal()-getSkips())) / getTotal()));
	}
	
	public void addSkips(int skips) {
		total+=skips;
		this.skips+=skips;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getRuns() {
		return total - skips;
	}
	
	public boolean isUnstable(){
		return (passes>0) && (fails > 0);
	}
	
	public void add(Result result){
		switch (result.getStatus()){
		case PASS:
			addPasses(1);
			break;
		case FAIL:
			addFails(1);
			break;
		case SKIP:
			addSkips(1);
			break;
		case STARTED:
			break;
		case ERROR:
			addFails(1);
			break;
		default:
			break;
		}
	}
	
	public void add(List<Result> results){
		for(Result result : results){
			add(result);
		}
	}
	
	public void add(RequirementTestSummary summary){
		addPasses(summary.getPasses());
		addFails(summary.getFails());
		addSkips(summary.getSkips());
	}
	
	public void merge(RequirementTestSummary summary){
		if(summary.getTotal()==0){
			return;
		}
		if(summary.getRuns()==0){
			return;
		}
		total++;
		if((summary.getFails()==0) && (summary.getPasses()>0)){
			passes++;
		}
		if((summary.getPasses()==0) && (summary.getFails()>0)){
			fails++;
		}
	}
}
