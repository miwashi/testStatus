package com.miwashi.model;

import com.miwashi.config.ApplicationConfig;
import com.miwashi.repositories.RequirementRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.miwashi.util.StaticUtil.*;

@Entity(name="REQUIREMENTS")
public class Requirement {

    @Column(name = "REQUIREMENT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String key = "key";
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("STARTTIME ASC")
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

    public void add(Result result){
        result.setRequirementId(getId());
        results.add(result);
    }

    public Result getLatestResult() {
    	return ((results==null) || (results.size()==0))?null:results.get(0);
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
            if(result.isSuccess()){
                numberOfSuccesses++;
            }
        }
        isUnstable = ((numberOfTests >0)&&(numberOfSuccesses > 0) &&(numberOfSuccesses!=numberOfTests));
        return isUnstable;
    }

    public boolean isSuccess(){
        boolean isSuccess = true;
        for(Result result : results){
            isSuccess = isSuccess && result.isSuccess();
        }
        return isSuccess;
    }

    public boolean isFailed(){
        boolean isFailed = true;
        for(Result result : results){
            isFailed = isFailed && result.isFail();
        }
        return isFailed;
    }

    public int getSuccessRate(){
        int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(!result.isSuccess()){
                fails++;
            }
        }
        return 100 - (runs==0?0:Math.round((100 * (runs-fails)) / runs));
    }
    
    public int getFailRate(){
        int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(!result.isSuccess()){
                fails++;
            }
        }
        return 100 - (runs==0?0:Math.round((100 * fails) / runs));
    }
    
    public int getNumberOfRuns(){
    	return results.size();
    }
    
    public int getNumberOfFails(){
    	int runs = 0;
        int fails = 0;
        for(Result result : results){
            runs++;
            if(result.isFail()){
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
            if(result.isSuccess()){
            	runs++;
            	if(runs>longestRun){
            		longestRun = runs;
            	}
            }else{
            	runs = 0;
            }
        }
        return longestRun;
    }
    
    public Date getFirstTested(){
    	if(results!=null && results.size()>0){
    		return results.get(0).getStartTime();
    	}
    	return this.getLatestTestedDate();
    }
    
    public String firstTestedStr(){
    	Date date = getFirstTested();
    	return ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getLatestTestedDate(){
    	Result lastResult = getLatestResult();
        if((lastResult==null) || (lastResult.getStartTime()==null)){
            return null;
        }
        return lastResult.getStartTime();
    }
    
    public String getLatestTestedDateAsStr(){
    	Date lastTested = getLatestTestedDate();
        return lastTested==null?"":ApplicationConfig.TIME_STAMP.format(lastTested);
    }
    
    public Date getLatestSuccessDate(){
    	for(Result result : results){
            if(result.isSuccess()){
            	return result.getStartTime();
            }
        }
    	return null;
    }
    
    public String getLatestSuccessDateAsStr(){
    	Date date = getLatestSuccessDate();
    	return date==null?"":ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Date getLatestFailDate(){
    	for(Result result : results){
            if(result.isFail()){
            	return result.getStartTime();
            }
        }
    	return null;
    }
    
    public String getLatestFailDateAsStr(){
    	Date date = getLatestFailDate();
    	return date==null?"":ApplicationConfig.TIME_STAMP.format(date);
    }
    
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    
    public String getNotTestedSince() {
        return toSince(this.getLatestTestedDate());
    }

    public int getNotTestedSinceDays(){
    	return toSinceDays(getLatestTestedDate());
    }

    public int getNotTestedSinceHours(){
    	return toSinceHours(getLatestTestedDate());
    }

    public int getNotTestedSinceMinutes(){
    	return toSinceMinutes(getLatestTestedDate());
    }

    public long getNotTestedSinceSeconds(){
    	return toSinceSeconds(getLatestTestedDate());
    }
    
    public int getDuration(){
    	int duration = 0;
    	if(results==null || results.size()==0){
    		return duration;
    	}
    	for(Result result : results){
    		duration += result.getDuration();
    	}
    	return Math.floorDiv(duration, results.size());
    }
    
    public Boolean[] getTestLights(){
    	Boolean b0 = results.size()>0?results.get(0).isSuccess():null;
    	Boolean b1 = results.size()>1?results.get(1).isSuccess():null;
    	Boolean b2 = results.size()>2?results.get(2).isSuccess():null;
    	Boolean b3 = results.size()>3?results.get(3).isSuccess():null;
    	Boolean b4 = results.size()>4?results.get(4).isSuccess():null;
    	Boolean b5 = results.size()>5?results.get(5).isSuccess():null;
    	Boolean b6 = results.size()>6?results.get(6).isSuccess():null;
    	Boolean b7 = results.size()>7?results.get(7).isSuccess():null;
    	Boolean b8 = results.size()>8?results.get(8).isSuccess():null;
    	Boolean b9 = results.size()>9?results.get(9).isSuccess():null;
    	return new Boolean[]{b0,b1,b2,b3,b4,b5,b6,b7,b8,b9};
    }
}
