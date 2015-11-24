package com.miwashi.jsonapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.jsonapi.statistics.DurationStat;
import com.miwashi.jsonapi.statistics.RequirementResultSummary;
import com.miwashi.jsonapi.statistics.RequirementTestSummary;
import com.miwashi.model.Job;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;

public class SimplePackageWithRequirements extends SimpleGroup {
	
	private List<SimpleRequirement> requirements = new ArrayList<SimpleRequirement>();
	
	public SimplePackageWithRequirements(long id, String name){
		super(id, name);
	}
	
	@Override
	public void add(Requirement  persistentRequirement){
		List<Result> persistentResults = persistentRequirement.getResults();
		SimpleRequirement requirement = new SimpleRequirement(persistentRequirement);
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
	
	public void clean(){
		for(SimpleRequirement requirement : getRequirements()){
			requirement.getResults().clear();
		}
		
		if("fails".equalsIgnoreCase(getName())){
			
			List<SimpleRequirement> toBeDeleted = new ArrayList<SimpleRequirement>();
			for(SimpleRequirement requirement : getRequirements()){
				RequirementResultSummary summary = (RequirementResultSummary) requirement.getStatistics().get("summary");
				if((summary!=null) && (summary.getFails()==0)){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
			
			Collections.sort(getRequirements(), new Comparator<SimpleRequirement>() {
	            @Override
	            public int compare(SimpleRequirement req1, SimpleRequirement req2) {
	                if (req1 == null || req2 == null || req1.getWhenStatusChanged() == null || req2.getWhenStatusChanged() == null) {
	                    return 0;
	                }
	                return req1.getWhenStatusChanged().getTime().compareTo(req2.getWhenStatusChanged().getTime());
	            }
	        });
			
			toBeDeleted = new ArrayList<SimpleRequirement>();
			int indX = 0;
			for(SimpleRequirement requirement : getRequirements()){
				if(indX++>9){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
		}
		
		if("unstable".equalsIgnoreCase(getName())){
			
			List<SimpleRequirement> toBeDeleted = new ArrayList<SimpleRequirement>();
			for(SimpleRequirement requirement : getRequirements()){
				RequirementResultSummary summary = (RequirementResultSummary) requirement.getStatistics().get("summary");
				if((summary!=null) && (!summary.isUnstable())){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
			
			Collections.sort(getRequirements(), new Comparator<SimpleRequirement>() {
	            @Override
	            public int compare(SimpleRequirement req1, SimpleRequirement req2) {
	                if (req1 == null || req2 == null) {
	                    return 0;
	                }
	                RequirementResultSummary summary1 = (RequirementResultSummary) req1.getStatistics().get("summary");
	                RequirementResultSummary summary2 = (RequirementResultSummary) req2.getStatistics().get("summary");
	                if (summary1 == null || summary2 == null) {
	                	return 0;
	                }
	                return Integer.compare(summary2.getFailRatio(), summary1.getFailRatio());
	            }
	        });
			
			toBeDeleted = new ArrayList<SimpleRequirement>();
			int indX = 0;
			for(SimpleRequirement requirement : getRequirements()){
				if(indX++>9){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
		}
		
		if("slow".equalsIgnoreCase(getName())){
			
			List<SimpleRequirement> toBeDeleted = new ArrayList<SimpleRequirement>();
			for(SimpleRequirement requirement : getRequirements()){
				DurationStat summary = (DurationStat) requirement.getStatistics().get("duration");
				if((summary!=null) && (summary.getN()<2)){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
			
			Collections.sort(getRequirements(), new Comparator<SimpleRequirement>() {
	            @Override
	            public int compare(SimpleRequirement req1, SimpleRequirement req2) {
	                if (req1 == null || req2 == null) {
	                    return 0;
	                }
	                DurationStat summary1 = (DurationStat) req1.getStatistics().get("duration");
	                DurationStat summary2 = (DurationStat) req2.getStatistics().get("duration");
	                if (summary1 == null || summary2 == null) {
	                	return 0;
	                }
	                return Long.compare(summary2.getAvg(), summary1.getAvg());
	            }
	        });
			
			toBeDeleted = new ArrayList<SimpleRequirement>();
			int indX = 0;
			for(SimpleRequirement requirement : getRequirements()){
				if(indX++>9){
					toBeDeleted.add(requirement);
				}
			}
			getRequirements().removeAll(toBeDeleted);
			
		}
	}
}
