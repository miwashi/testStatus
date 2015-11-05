package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Requirement;
import com.miwashi.model.Result;

public class SummaryRequirement extends AbstractSummary{
	
	private List<SimpleResult> results = new ArrayList<SimpleResult>();
	private SimpleRequirement requirement;
	private SimpleTeam team;
	private SimpleGroup group;
	
	public SummaryRequirement(Requirement persistentRequirement) {
		requirement = new SimpleRequirement(persistentRequirement);
		requirement.add(persistentRequirement.getResults());
		team = new SimpleTeam(persistentRequirement.getGroup());
		group = new SimpleGroup(persistentRequirement.getSubGroup());
		isTeamInclued = true;
		isGroupIncluded = true;
	}

	public List<SimpleResult> getResults() {
		return results;
	}
	
	public SimpleRequirement getRequirement() {
		return requirement;
	}

	public SimpleTeam getTeam() {
		return team;
	}

	public SimpleGroup getGroup() {
		return group;
	}
	
	
}
