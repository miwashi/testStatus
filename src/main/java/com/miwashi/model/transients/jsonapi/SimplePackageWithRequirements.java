package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Requirement;
import com.miwashi.model.Result;

public class SimplePackageWithRequirements extends SimpleGroup {
	
	private List<SimpleRequirement> requirements = new ArrayList<SimpleRequirement>();
	
	@Override
	public void add(Requirement  persistentRequirement){
		List<Result> persistentResults = persistentRequirement.getResults();
		SimpleRequirement requirement = new SimpleRequirement(persistentRequirement.getId(), persistentRequirement.getKey());
		requirement.add(persistentResults);
		requirements.add(requirement);
		RequirementTestSummary requirementSummary = (RequirementTestSummary) requirement.getStatistics().get("summary");
		if(requirementSummary!=null){
			RequirementTestSummary groupSummary = (RequirementTestSummary) getStatistics().get("summary");
			if(groupSummary==null){
				groupSummary = new RequirementTestSummary();
				getStatistics().put("summary",groupSummary);
			}
			groupSummary.add(requirementSummary);
		}
	}
	
	public List<SimpleRequirement> getRequirements() {
		return requirements;
	}
	
	public int getNumberOfRequirements(){
		return requirements!=null?requirements.size():0;
	}
}
