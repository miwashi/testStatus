package com.miwashi.jsonapi.summaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.miwashi.jsonapi.SimpleJob;
import com.miwashi.jsonapi.statistics.GroupTestSummary;
import com.miwashi.model.Job;

public class JobSummary extends AbstractSummary{

	private String name;
	private List<SimpleJob> jobs = new ArrayList<SimpleJob>();
	
	public JobSummary(String name){
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<SimpleJob> getJobs(){
		return jobs;
	}

	public void add(Job persistentJob){
		SimpleJob job = new SimpleJob(persistentJob);
		jobs.add(job);
	}

	public void clean() {
		Collections.sort(jobs, new Comparator<SimpleJob>() {
            @Override
            public int compare(SimpleJob result1, SimpleJob result2) {
                if (result1 == null || result2 == null || result1.getWhen() == null || result2.getWhen() == null) {
                    return 0;
                }
                return result2.getWhen().compareTo(result1.getWhen());
            }
        });
	}
}
