package net.miwashi.jsonapi.statistics;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.miwashi.jsonapi.SimpleResult;
import net.miwashi.jsonapi.SimpleTimeStamp;
import net.miwashi.teststatus.model.Result;
import net.miwashi.teststatus.model.TestState;

public class LastResult {
	
	private SimpleTimeStamp when = new SimpleTimeStamp(null);
	private String status = TestState.UNKNOWN.toString();
	

	public LastResult(List<Result> results){
		if(results==null){
			return;
		}
		Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result result1, Result result2) {
                if (result1 == null || result2 == null || result1.getStartTime() == null || result2.getStartTime() == null) {
                    return 0;
                }
                return result2.getStartTime().compareTo(result2.getStartTime());
            }
        });
		
		int numFail = 0;
		int numConcecutiveFail = 0;
		int numConcecutivePass = 0;
		int numPass = 0;
		
		for(Result result : results){
			if(result.isFail()){
				numFail++;
				if(numPass==0){
					numConcecutiveFail++;
				}
			}			
			if(result.isSuccess()){
				numPass++;
				if(numFail==0){
					numConcecutivePass++;
				}
			}
		}
		boolean isUnstable = (numFail > 0) && (numPass > 0);
		boolean isUntested = results.size()<=1;
		TestState lastStatus = results.get(0)==null?TestState.UNKNOWN:results.get(0).getStatus();
		
		if(lastStatus.equals(TestState.PASS)){
			if(isUntested){
				status = "first_" + TestState.PASS.name();
			} else if((numConcecutivePass>2) && isUnstable){
				status = "stable_" + TestState.PASS.name();
			} else if(isUnstable){
				status = "unstable_" + TestState.PASS.name();
			}else {
				status = "stable_" + TestState.PASS.name();
			}
		}else if(lastStatus.equals(TestState.FAIL)){
			if(isUntested){
				status = TestState.FAIL.name();
			} else if((numConcecutiveFail>2) && isUnstable){
				status = "stable_" + TestState.FAIL.name();
			} else if(isUnstable){
				status = "unstable_" + TestState.FAIL.name();
			}else {
				status = "stable_" + TestState.FAIL.name();
			}
		}
		
		when = new SimpleTimeStamp(results.get(0)==null?null:results.get(0).getStartTime());
	}
	
	public SimpleTimeStamp getWhen(){
		return when;
	}
	
	public String getStatus(){
		return status;
	}
}
