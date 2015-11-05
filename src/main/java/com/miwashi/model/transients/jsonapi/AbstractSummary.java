package com.miwashi.model.transients.jsonapi;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSummary {
	
	private Map<String, Object> statistics = new HashMap<String, Object>();
	protected boolean isTeamInclued = false;
	protected boolean isGroupIncluded = false;
	
	public boolean isGroupIncluded(){
		return this.isGroupIncluded;
	}
	
	public boolean isTeamIncluded(){
		return this.isTeamInclued;
	}
	
	public Map<String, Object> getStatistics() {
		return statistics;
	}
}
