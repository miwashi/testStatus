package com.miwashi.jsonapi.summaries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miwashi.jsonapi.statistics.GroupTestSummary;
import com.miwashi.model.Job;

public class JobsSummary extends AbstractSummary{
	
	Map<String, JobSummary> jobSummaries = new HashMap<String, JobSummary>();
	
	public void add(JobSummary jobSummary){
		jobSummaries.put(jobSummary.getName(), jobSummary);
	}
	
	public void add(Collection<JobSummary> jobSummaries){
		
	}
	
	public void add(Job persistentJob){
		if(persistentJob==null || persistentJob.getName() == null || persistentJob.getName().isEmpty()){
			return;
		}
		JobSummary jobSummary = jobSummaries.get(persistentJob.getName());
		if(jobSummary==null){
			jobSummary = new JobSummary(persistentJob.getName());
			jobSummaries.put(persistentJob.getName(),jobSummary);
		}
		jobSummary.add(persistentJob);
	}
	
	public List<JobSummary> getJobs(){
		List<JobSummary> result = new ArrayList<JobSummary>();
		result.addAll(jobSummaries.values());
		return result;
	}

	public void clean() {
		for(JobSummary jobSummary : jobSummaries.values()){
			jobSummary.clean();
		}
	}

}
