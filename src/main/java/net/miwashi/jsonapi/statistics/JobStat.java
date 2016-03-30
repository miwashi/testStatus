package net.miwashi.jsonapi.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.miwashi.jsonapi.summaries.KeySummary;
import net.miwashi.teststatus.model.Result;

public class JobStat extends AbstractStatistics {

	private Set<String> keys = new HashSet<String>();
	private Map<String, KeySummary> summaries = new HashMap<String, KeySummary>();
	
	public JobStat(String name){
		super(name);
		
	}

	public void add(String key, List<Result> results) {
		keys.add(key);
		KeySummary summary = summaries.get(key);
		if(summary == null){
			summaries.put(key, new KeySummary(key));
		}
	}
	
	public void add(String key, Result result) {
		keys.add(key);
		KeySummary summary = summaries.get(key);
		if(summary == null){
			summaries.put(key, new KeySummary(key));
		}
	}
	
	public Collection<String> getKeys(){
		return keys;
	}
	
	public Collection<KeySummary> getSummaries(){
		return summaries.values();
	}
}
