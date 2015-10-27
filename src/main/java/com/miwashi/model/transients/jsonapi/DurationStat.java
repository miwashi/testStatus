package com.miwashi.model.transients.jsonapi;

import java.util.ArrayList;
import java.util.List;

import com.miwashi.model.Result;
import com.miwashi.model.TestState;

public class DurationStat {
	
	private long sum = 0;
	private double stdev = 0;
	private double var = 0;
	private double avg = 0;
	private double margin = 0;
	private double marginUpper = 0;
	private double marginLower = 0;
	private double sumLn = 0;
	private double avgLn = 0;
	private double varLn = 0;
	private double stdevLn = 0;
	
	private List<Long> data = new ArrayList<Long>();
	private boolean isDirty = true;
	
	public DurationStat(List<Result> results, TestState lookFor){
		for(Result result : results){
			this.add(result, lookFor);
		}
	}
	
	public DurationStat(List<Result> results){
		for(Result result : results){
			this.add(result, TestState.PASS);
		}
	}
	
	private void reset(){
		sum = 0;
		var = 0;
		sumLn = 0;
		varLn = 0;
		
		if(data.size()<=0){
			return;
		}
		
		for(Long x : data){
			sum+= x;
			sumLn+= Math.log(x);
		}
		avg = (sum / data.size());
		avgLn = (sumLn / data.size());
		
		for(Long x : data){
			var+= (x - avg)*(x - avg);
			varLn+= (Math.log(x) - avgLn)*(Math.log(x) - avgLn);
		}
		stdev = Math.sqrt(var);
		stdevLn = Math.sqrt(varLn);
		
		margin = (1.96) * stdev / Math.sqrt(data.size());
		marginUpper = Math.exp(avgLn + (1.96) * stdevLn / Math.sqrt(data.size()));
		marginLower = Math.exp(avgLn - (1.96) * stdevLn / Math.sqrt(data.size()));
		
		isDirty = false;
	}
	
	public void add(long x){
		isDirty = true;
		data.add(x);
	}
	
	public void add(Result result, TestState lookFor){
		if(result.getStatus() == lookFor){
			isDirty = true;
			data.add(result.getDuration());
		}
	}
	
	public long getSum(){
		if(isDirty){
			reset();
		}
		return sum;
	}
	
	public long getAvg(){
		if(isDirty){
			reset();
		}
		return (long) avg;
	}
	
	public long getStdev(){
		if(isDirty){
			reset();
		}
		return (long) stdev;
	}
	
	public long getMarginOfError(){
		if(isDirty){
			reset();
		}
		return (long) margin;
	}
	
	public long getLimitLower(){
		if(isDirty){
			reset();
		}
		return (long) marginLower;
	}
	
	public long getLimitUpper(){
		if(isDirty){
			reset();
		}
		return (long) marginUpper;
	}
	
	public int getN(){
		return data.size();
	}
	
	public String getIntervall(){
		if(isDirty){
			reset();
		}
		String result = ((long) (avg / 1000)) + "s (" + ((long) (marginLower/1000) + "s-" + ((long) (marginUpper/1000) + "s)"));
		return result;
	}
}
