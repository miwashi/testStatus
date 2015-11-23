package com.miwashi.jsonapi.summaries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.jsonapi.SimpleGroup;
import com.miwashi.jsonapi.SimpleLink;
import com.miwashi.jsonapi.SimpleRequirement;
import com.miwashi.jsonapi.SimpleResult;
import com.miwashi.jsonapi.SimpleTeam;
import com.miwashi.jsonapi.SimpleLink.Ref;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;

public class RequirementSummary extends AbstractSummary{
	
	private List<SimpleResult> results = new ArrayList<SimpleResult>();
	private SimpleRequirement requirement;
	private SimpleTeam team;
	private SimpleGroup group;
	
	public RequirementSummary(Requirement persistentRequirement) {
		requirement = new SimpleRequirement(persistentRequirement);
		requirement.add(persistentRequirement.getResults());
		team = new SimpleTeam(persistentRequirement.getGroup());
		group = new SimpleGroup(persistentRequirement.getSubGroup());
		links.add(links.new Ref("/group/" + team.getId(), team.getName()),links.new Ref("/group/" + group.getId(), group.getName()));
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
