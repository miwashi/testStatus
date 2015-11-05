package com.miwashi.model.transients.jsonapi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.model.transients.jsonapi.ParameterizedRequirement.Parameter;

public class ParameterizedRequirementSummary  extends AbstractSummary{
	private Map<String, ParameterizedRequirement> requirements = new HashMap<String, ParameterizedRequirement>();
	private RequirementTestSummary summary = new RequirementTestSummary();
	
	public Collection<ParameterizedRequirement> getRequirements(){
		return requirements.values();
	}
	
	public RequirementTestSummary getSummary() {
		return summary;
	}
	
	/**
	 * 
	 * @param requirement
	 */
	public void add(Requirement requirement){
		final String method = requirement.getSubject().getName() + " " + requirement.getTestRequirement();
		final String requirementAsText = parseForRequirement(method);
		final String parameter = parseForParameter(method);
		
		List<Result> results = requirement.getResults();
		ParameterizedRequirement paramRequirement = addToRequirement(requirement.getSubject().getId(), requirementAsText);
		paramRequirement.add(paramRequirement.new Parameter(requirement.getId(), parameter, results));
		summary.add(paramRequirement.getSummary());
	}
	
	
	

	private String parseForRequirement(String requirementAsText){
		String requirement = requirementAsText;
		if((requirementAsText.indexOf("[")>=0) && (requirementAsText.indexOf("]")>=0)){
			requirement = requirementAsText.substring(0, requirementAsText.indexOf("["));
		}
		return requirement;
	}
	
	private String parseForParameter(String requirementAsText){
		String parameter = "";
		if((requirementAsText.indexOf("[")>=0) && (requirementAsText.indexOf("]")>=0)){
			parameter = requirementAsText.substring(requirementAsText.indexOf("["));
		}
		return parameter;
	}

	private ParameterizedRequirement addToRequirement(long id, String test){
		ParameterizedRequirement paramRequirement = null;
		if(requirements.containsKey(test)){
			paramRequirement = requirements.get(test);
		}else{
			paramRequirement = new ParameterizedRequirement(id, test);
			requirements.put(test, paramRequirement);
		}
		return paramRequirement;
	}
}
