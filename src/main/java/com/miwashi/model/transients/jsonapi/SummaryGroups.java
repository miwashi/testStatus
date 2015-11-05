package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Group;

public class SummaryGroups extends AbstractSummary{
	
	private List<SimpleTeam> teams = new ArrayList<SimpleTeam>();
	private List<SimpleGroup> groups = new ArrayList<SimpleGroup>();
	
	public List<SimpleGroup> getGroups(){
		return groups;
	}
	
	public List<SimpleTeam> getTeams(){
		return teams;
	}
	
	public void add(Group persistentGroup){
		SimpleGroup group = new SimpleGroup();
		group.setId(persistentGroup.getId());
		group.setName(persistentGroup.getName());
		group.setTeamName(persistentGroup.getTopName());
		groups.add(group);
		if(persistentGroup.getName().indexOf(".")<0){
			SimpleTeam team = new SimpleTeam();
			team.setId(persistentGroup.getId());
			team.setName(persistentGroup.getName());
			teams.add(team);
		}
		
		RequirementTestSummary summary = (RequirementTestSummary) getStatistics().get("summary");
		if(summary==null){
			summary = new RequirementTestSummary();
			getStatistics().put("summary", summary);
		}
		RequirementTestSummary groupSummary = (RequirementTestSummary) group.getStatistics().get("summary");
		if(groupSummary!=null){
			summary.add(groupSummary);
		}
	}
	
	public void clean(){
		List<SimpleGroup> toBeDeleted = new ArrayList<SimpleGroup>();
		for(SimpleGroup group : groups){
			if(group.getNumberOfRequirements()==0){
				toBeDeleted.add(group);
			}
		}
		groups.removeAll(toBeDeleted);
	}
}
