package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Group;
import com.miwashi.model.Requirement;

public class SummaryGroup extends AbstractSummary{
	
	private List<SimpleRequirement> requirements = new ArrayList<SimpleRequirement>();
	private SimpleGroup group = new SimpleGroup(); 
	
	public SummaryGroup(){
		getStatistics().put("summary", new GroupTestSummary());
	}
	
	public List<SimpleRequirement> getRequirements(){
		return requirements;
	}
	
	public void add(Requirement persistentRequirement){
		SimpleRequirement requirement = new SimpleRequirement(persistentRequirement);
		requirements.add(requirement);
		requirement.add(persistentRequirement.getResults());
		
		;
		
		GroupTestSummary summary = (GroupTestSummary) getStatistics().get("summary");
		if(summary==null){
			summary = new GroupTestSummary();
			getStatistics().put("summary", summary);
		}
		
		RequirementTestSummary requirementSummary = (RequirementTestSummary) requirement.getStatistics().get("summary");
		if(requirementSummary!=null){
			summary.merge(requirementSummary);
		}
	}
	
	public void add(Group persistentGroup){
		group.setId(persistentGroup.getId());
		group.setName(persistentGroup.getName());
		group.setTeamName(persistentGroup.getTopName());
	}
	
	public SimpleGroup getGroup(){
		return group;
	}
	
	public void clean(){
		Collections.sort(requirements, new Comparator<SimpleRequirement>() {
            @Override
            public int compare(SimpleRequirement result1, SimpleRequirement result2) {
                if (result1 == null || result2 == null || result1.getName() == null || result2.getName() == null) {
                    return 0;
                }
                return result2.getName().compareTo(result1.getName());
            }
        });
	}
}
