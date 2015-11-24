package com.miwashi.jsonapi.summaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.miwashi.jsonapi.SimpleJob;
import com.miwashi.jsonapi.statistics.DurationStat;
import com.miwashi.jsonapi.statistics.GroupTestSummary;
import com.miwashi.jsonapi.statistics.RunStats;
import com.miwashi.model.Job;

public class JobSummary extends AbstractSummary{

	private String name;
	private String key;
	private List<SimpleJob> jobs = new ArrayList<SimpleJob>();
	
	public JobSummary(String name){
		super();
		this.name = name;
		key = name;
		key = key.replaceAll("[^A-Za-z0-9]", "_");
	}
	
	public String getName() {
		return name;
	}
	
	public String getKey() {
		return key;
	}
	
	public List<SimpleJob> getJobs(){
		return jobs;
	}

	public void add(Job persistentJob){
		SimpleJob job = new SimpleJob(persistentJob);
		jobs.add(job);
		
		RunStats runStats = (RunStats) getStatistics().get("runs");
		if(runStats == null){
			runStats = new RunStats();
			getStatistics().put("runs", runStats);
		}
		runStats.add(job);
		
		DurationStat durationStats = (DurationStat) getStatistics().get("duration");
		if(durationStats == null){
			durationStats = new DurationStat();
			getStatistics().put("duration", durationStats);
		}
		durationStats.add(job);
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
