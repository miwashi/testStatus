package com.miwashi.model.transients.jsonapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Group;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;

public class SimpleGroup extends SimpleTeam {
	
	private long id;
	private String name;
	private String teamName;
	private int numberOfRequirements = 0;
	
	private Map<String, Object> statistics = new HashMap<String, Object>();
	
	public SimpleGroup(Group subGroup) {
		super(subGroup);
	}

	public SimpleGroup() {
		super();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public int getNumberOfRequirements(){
		return numberOfRequirements;
	}
	
	public void add(List<Requirement>  requirements){
		for(Requirement requirement : requirements){
			add(requirement);
		}
	}
	
	public void add(Requirement  persistentRequirement){
		numberOfRequirements++;
		List<Result> persistentResults = persistentRequirement.getResults();
		SimpleRequirement requirement = new SimpleRequirement(persistentRequirement.getId(), persistentRequirement.getKey());
		requirement.add(persistentResults);
		
		RequirementTestSummary requirementSummary = (RequirementTestSummary) requirement.getStatistics().get("summary");
		if(requirementSummary!=null){
			GroupTestSummary groupSummary = (GroupTestSummary) getStatistics().get("summary");
			if(groupSummary==null){
				groupSummary = new GroupTestSummary();
				getStatistics().put("summary",groupSummary);
			}
			groupSummary.merge(requirementSummary);
		}
		
		DurationStat requirementDuration = (DurationStat) requirement.getStatistics().get("duration");
		if(requirementDuration!=null){
			DurationStat groupDuration = (DurationStat) getStatistics().get("duration");
			if(groupDuration==null){
				groupDuration = new DurationStat();
				getStatistics().put("duration",groupDuration);
			}
			groupDuration.add(requirementDuration);
		}
	}
	
	public Map<String, Object> getStatistics(){
		return statistics;
	}
}
