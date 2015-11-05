package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class ParameterizedRequirement {
	private long id;
	private String requirement;
	private RequirementTestSummary summary = new RequirementTestSummary();
	
	private List<Parameter> parameters = new ArrayList<Parameter>();
	
	public class Parameter{
		private long id;
		private String name;
		private DurationStat passStat;
		private DurationStat failStat;
		private RequirementStat requirementStat;
		
		private List<SimpleResult> results = new ArrayList<SimpleResult>();
		
		public Parameter(long id, String name, List<Result> persistentResults) {
			this.id = id;
			this.name = name;
			passStat = new DurationStat(persistentResults);
			failStat = new DurationStat(persistentResults, TestState.FAIL);
			//requirementStat = new RequirementStat(persistentResults);
			
			for(Result persistentResult : persistentResults){
				SimpleResult result = new SimpleResult(persistentResult);
				results.add(result);
				summary.add(persistentResult);
			}
			
			
			Collections.sort(results, new Comparator<SimpleResult>() {
	            @Override
	            public int compare(SimpleResult result1, SimpleResult result2) {
	                if (result1 == null || result2 == null || result1.getWhen() == null || result2.getWhen() == null) {
	                    return 0;
	                }
	                return result2.getWhen().getTime().compareTo(result1.getWhen().getTime());
	            }
	        });
			
		}
		
		public long getId(){
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public DurationStat getDuration() {
			return passStat;
		}
		
		public DurationStat getDurationForFails() {
			return failStat;
		}
		
		public RequirementStat getRunStat() {
			return requirementStat;
		}
		
		
		
		public RequirementTestSummary getSummary() {
			return summary;
		}

		public List<SimpleResult> getResult(){
			return results;
		}

	}

	public ParameterizedRequirement(long id, String requirement) {
		this.id = id;
		this.requirement = requirement;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void add(Parameter parameter) {
		this.parameters.add(parameter);
	}

	public long getId() {
		return id;
	}
	
	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public RequirementTestSummary getSummary() {
		return summary;
	}

	public void add(Result result){
		
	}
}
