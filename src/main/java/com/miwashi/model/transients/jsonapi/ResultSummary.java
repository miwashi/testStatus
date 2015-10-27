package com.miwashi.model.transients.jsonapi;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class ResultSummary {
	
	int total = 0;
	int passes = 0;
	int fails = 0;
	int skips = 0;
	int unstable = 0;
	
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
	
	public int getUnstable() {
		return unstable;
	}
	
	public int getUstableRatio() {
		return 100 - (getRuns()==0?0:Math.round((100 * (getRuns()-getUnstable())) / getRuns()));
	}
	
	public void addUnstable(int unstable) {
		this.unstable+=unstable;
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
	
	public void add(ResultSummary summary){
		addPasses(summary.getPasses());
		addFails(summary.getFails());
		addSkips(summary.getSkips());
		addUnstable(summary.getUnstable());
	}
}
