package net.miwashi.jsonapi.statistics;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.miwashi.jsonapi.summaries.KeySummary;
import net.miwashi.teststatus.model.Requirement;

public class KeyStat extends AbstractStatistics {

	private int numRequirements = 0;
	private int numJobs = 0;
	private int numTtests = 0;
	
	private Set<String> keys = new HashSet<String>();
	private Map<String, KeySummary> summaries = new HashMap<String, KeySummary>();
	
	public KeyStat(String name){
		super(name);
		
	}

	public void add(String key, Requirement requirement) {
		keys.add(key);
		KeySummary summary = summaries.get(key);
		if(summary == null){
			summary = new KeySummary(key);
			summaries.put(key, summary);
		}
		numRequirements++;
		summary.add(requirement);
	}
	
	public Collection<String> getKeys(){
		return keys;
	}
	
	public Collection<KeySummary> getSummaries(){
		return summaries.values();
	}

	public int getNumRequirements() {
		return numRequirements;
	}

	public int getNumJobs() {
		return numJobs;
	}

	public int getNumTtests() {
		return numTtests;
	}
	
	
}
