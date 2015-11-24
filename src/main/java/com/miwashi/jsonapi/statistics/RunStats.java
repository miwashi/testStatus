package com.miwashi.jsonapi.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.miwashi.jsonapi.SimpleJob;
import com.miwashi.jsonapi.SimpleTimeStamp;
import com.miwashi.model.Job;
import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class RunStats {
	
	private double sum = 0;
	private double stdev = 0;
	private double var = 0;
	private double avg = 0;
	private double margin = 0;
	private double marginUpper = 0;
	private double marginLower = 0;
	
	private int passes;
	private int fails;
	
	Date lastRun;
	Date firstRun;
	Date lastPass;
	Date lastFail;
	
	private List<Long> data = new ArrayList<Long>();
	private boolean isDirty = true;
	
	public RunStats(){
		
	}
	
	public RunStats(List<Job> persistentJobs) {
		
	}
	
	private void reset(){
		sum = 0;
		var = 0;
		
		if(data.size()<=0){
			return;
		}
		
		for(Long x : data){
			sum+= x;
		}
		avg = (sum / data.size());		
		for(Long x : data){
			var+= (x - avg)*(x - avg);
		}
		stdev = Math.sqrt(var);
		
		margin = (1.96) * stdev / Math.sqrt(data.size());
		marginUpper = avg + (1.96) * stdev / Math.sqrt(data.size());
		marginLower = avg - (1.96) * stdev / Math.sqrt(data.size());
		
		isDirty = false;
	}
	
	public void add(SimpleJob job){
		isDirty = true;
		long jobFails = job.getJenkinsStat()!=null?job.getJenkinsStat().getFails():0;
		
		data.add(jobFails);
		if(jobFails>0){
			fails++;
			if(lastFail==null && job.getWhen()!=null){
				lastFail = job.getWhen().getTime();
			}else if(job.getWhen()!=null && job.getWhen().getTime()!=null && job.getWhen().getTime().getTime() > lastFail.getTime()){
				lastFail = job.getWhen().getTime();
			}
		}else{
			passes++;
			if(lastPass==null && job.getWhen()!=null){
				lastPass = job.getWhen().getTime();
			}else if(job.getWhen()!=null && job.getWhen().getTime()!=null && job.getWhen().getTime().getTime() > lastPass.getTime()){
				lastPass = job.getWhen().getTime();
			}
		}
		if(lastRun==null && job.getWhen()!=null){
			lastRun = job.getWhen().getTime();
		}else if(job.getWhen()!=null && job.getWhen().getTime()!=null && job.getWhen().getTime().getTime() > lastRun.getTime()){
			lastRun = job.getWhen().getTime();
		}
		if(firstRun==null && job.getWhen()!=null){
			firstRun = job.getWhen().getTime();
		}else if(job.getWhen()!=null && job.getWhen().getTime()!=null && job.getWhen().getTime().getTime() < firstRun.getTime()){
			firstRun = job.getWhen().getTime();
		}
	}
	
	public double getSum(){
		if(isDirty){
			reset();
		}
		return sum;
	}
	
	public double getAvg(){
		if(isDirty){
			reset();
		}
		return avg;
	}
	
	public double getStdev(){
		if(isDirty){
			reset();
		}
		return stdev;
	}
	
	public double getMarginOfError(){
		if(isDirty){
			reset();
		}
		return (long) margin;
	}
	
	public double getLimitLower(){
		if(isDirty){
			reset();
		}
		return marginLower;
	}
	
	public double getLimitUpper(){
		if(isDirty){
			reset();
		}
		return marginUpper;
	}
	
	public int getN(){
		return data.size();
	}
	
	public String getIntervall(){
		if(isDirty){
			reset();
		}
		String result = ( (avg / 1000)) + " (" + ( (marginLower/1000) + "-" + ( (marginUpper/1000) + ")"));
		return result;
	}
	
	public int getPasses() {
		return passes;
	}
	public void setPasses(int passes) {
		this.passes = passes;
	}
	public int getFails() {
		return fails;
	}
	public void setFails(int fails) {
		this.fails = fails;
	}

	public SimpleTimeStamp getLastRun(){
		return new SimpleTimeStamp(lastRun);
	}
	
	public SimpleTimeStamp getLastFail(){
		return new SimpleTimeStamp(lastFail);
	}
	
	public SimpleTimeStamp getLastPass(){
		return new SimpleTimeStamp(lastPass);
	}
	
	public SimpleTimeStamp getFirstRun(){
		return new SimpleTimeStamp(firstRun);
	}
	
	public String getStatus(){
		int total = fails + passes;
		String ratio = "" + (100 - (total==0?0:Math.round((100 * (total-getPasses())) / total)));
		return "" + (fails>0?"FAIL - ":"SUCCESS - ") + passes + "/" + total + " (" + ratio + "%)";
	}
}
