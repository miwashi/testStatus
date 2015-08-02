package com.miwashi.model;

import com.miwashi.config.ApplicationConfig;
import com.miwashi.repositories.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity(name="REQUIREMENTS")
public class Requirement {

    @Column(name = "REQUIREMENT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String key = "key";
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastTested;

    public Date getLastTested() {
		return lastTested;
	}

	public void setLastTested(Date lastTested) {
		this.lastTested = lastTested;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQURIEMENT_ID", nullable = false)
    private List<Result> results = new ArrayList<Result>();

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBGROUP_ID")
    private Group subGroup;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SUJBECT_ID")
    private Subject subject;

    @Transient
    private Result lastResult;

    public Requirement(){
        super();
    }

    public Requirement(String key){
        super();
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void add(Result result){
        result.setRequirementId(getId());
        results.add(result);
    }

    public Result getLastResult() {

        long latest = 0;
        lastResult = null;
        for(Result result : results){
            if(latest < result.getStartTime().getTime()){
                lastResult = result;
                latest = result.getStartTime().getTime();
            }
        }
        return lastResult;
    }

    public void setLastResult(Result lastResult) {
        this.lastResult = lastResult;
    }

    public String getTestRequirement(){
        String testRequiement = "";
        if(key.indexOf(".")<0){
            return key;
        }
        testRequiement = key.substring(key.lastIndexOf("."));
        testRequiement = testRequiement.replaceAll("\\.", "");
        testRequiement = testRequiement.replaceAll("(.)([A-Z])", "$1 $2");
        testRequiement = testRequiement.toLowerCase();
        return testRequiement;
    }

    public Date lastTested(){
        if(getLastResult()==null){
            return null;
        }
        return getLastResult().getStartTime();
    }

    public String lastTestedStr(){
        if((getLastResult()==null) || (getLastResult().getStartTime()==null)){
            return "n/a";
        }
        return ApplicationConfig.TIME_STAMP.format(getLastResult().getStartTime());
    }

    public Group getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(Group subGroup) {
        this.subGroup = subGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isTested(){
        return results==null?false:results.size()>0;
    }

    public boolean isUnstable(){
        boolean isUnstable = false;
        int numberOfTests = 0;
        int numberOfSuccesses = 0;
        for(Result result : results){
            numberOfTests++;
            if(result.getStatus()){
                numberOfSuccesses++;
            }
        }
        isUnstable = ((numberOfTests >0)&&(numberOfSuccesses > 0) &&(numberOfSuccesses!=numberOfTests));
        return isUnstable;
    }

    public boolean isSuccess(){
        boolean isSuccess = true;
        for(Result result : results){
            isSuccess = isSuccess && result.getStatus();
        }
        return isSuccess;
    }

    public boolean isFailed(){
        boolean isFailed = true;
        for(Result result : results){
            isFailed = isFailed && !result.getStatus();
        }
        return isFailed;
    }

    public int getSuccessrate(){
        int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(!result.getStatus()){
                fails++;
            }
        }
        return 100 - (runs==0?0:Math.round((100 * fails) / runs));
    }
    
    public int getNumberOfRuns(){
    	return results.size();
    }
    
    public int getNumberOfFailes(){
    	int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(!result.getStatus()){
                fails++;
            }
        }
        return fails;
    }
    
    public int getLongestConsecutiveSuccess(){
    	int longestRun = 0;
    	int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(result.getStatus()){
            	runs++;
            }else{
            	if(runs > longestRun){
            		longestRun = runs;
            	}
            	runs = 0;
            }
        }
        return longestRun;
    }
    
    public Date getFirstTested(){
    	Date date = new Date();
    	for(Result result : results){
            Date testDate = result.getStartTime();
            if(date.compareTo(testDate)>0){
            	date = testDate;
            }
        }
    	return date;
    }
    
    public String firstTestedStr(){
    	Date date = getFirstTested();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getLastSuccess(){
    	Date date = new Date();
    	for(Result result : results){
            Date testDate = result.getStartTime();
            if(date.compareTo(testDate)>0){
            	date = testDate;
            }
        }
    	return date;
    }
    
    public String getLastSuccessStr(){
    	Date date = getLastSuccess();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getFirstSuccess(){
    	Date date = new Date();
    	for(Result result : results){
            Date testDate = result.getStartTime();
            if(date.compareTo(testDate)>0){
            	date = testDate;
            }
        }
    	return date;
    }
    
    public String getFirstSuccessStr(){
    	Date date = getFirstSuccess();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getLastFail(){
    	Date date = new Date();
    	for(Result result : results){
            Date testDate = result.getStartTime();
            if(date.compareTo(testDate)>0){
            	date = testDate;
            }
        }
    	return date;
    }
    
    public String getLastFailStr(){
    	Date date = getLastFail();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getFirstFail(){
    	Date date = new Date();
    	for(Result result : results){
            Date testDate = result.getStartTime();
            if(date.compareTo(testDate)>0){
            	date = testDate;
            }
        }
    	return date;
    }
    
    public String getFirstFailStr(){
    	Date date = getFirstFail();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
