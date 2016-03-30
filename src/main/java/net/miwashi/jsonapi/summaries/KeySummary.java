package net.miwashi.jsonapi.summaries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.miwashi.jsonapi.AbstractSummary;
import net.miwashi.jsonapi.SimpleRequirement;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;

public class KeySummary extends AbstractSummary{
	
	private List<SimpleRequirement> requirements = new ArrayList<SimpleRequirement>();
	private int numRequirements = 0;
	private int numJobs = 0;
	private int numTtests = 0;
	
	public KeySummary(String name){
		super(name);
	}
	
	public void add(Requirement requirement){
		requirements.add(new SimpleRequirement(requirement));
	}
	
	public void add(Result result){
		
	}
	
	public Collection<SimpleRequirement> getRequirements(){
		return requirements;
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
