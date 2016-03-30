package net.miwashi.teststatus.model;

import net.miwashi.config.ApplicationConfig;
import net.miwashi.teststatus.repositories.RequirementRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import static net.miwashi.util.StaticUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name="REQUIREMENTS")
public class Requirement {

    @Column(name = "REQUIREMENT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, length=1024)
    private String key = "key";
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("STARTTIME DESC")
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
    
    private Date statusChangeDate;
    private boolean status;
    
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
    
    public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public boolean isStatusPass() {
		return status;
	}

	public void setStatusPass(boolean status) {
		this.status = status;
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
        String testRequiement = key;
        if(key.indexOf(".")<0){
            return key;
        }
        
        String testParam = "";
        int pos1 = key.indexOf("[");
        int pos2 = key.indexOf("]");
        if((pos1>=0)&&(pos2>=0)){
        	testParam = key.substring(pos1, pos2 + 1);
        	testRequiement = key.substring(0, pos1);
        }
        
        testRequiement = testRequiement.substring(testRequiement.lastIndexOf("."));
        testRequiement = testRequiement.replaceAll("\\.", "");
        testRequiement = testRequiement.replaceAll("(.)([A-Z])", "$1 $2");
        testRequiement = testRequiement.toLowerCase();
        return testRequiement + (testParam.isEmpty()?"":" " + testParam);
    }
    
    public String getParameter(){
        String parameter = getTestRequirement();
        if((parameter.indexOf("[")>=0) && (parameter.indexOf("]")>=0)){
        	parameter = parameter.substring(parameter.indexOf("["));
    	}else{
    		parameter = "";
    	}
        parameter = parameter.replace("[", "");
        parameter = parameter.replace("]", "");
        return parameter;
    }
    
    public String getUnParameterizedTestRequirement(){
        String testRequiement = getTestRequirement();
        if((testRequiement.indexOf("[")>=0) && (testRequiement.indexOf("]")>=0)){
        	testRequiement = testRequiement.substring(0, testRequiement.indexOf("["));
    	}
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
        
        if(results.size() >0){
        	isUnstable = isUnstable && !results.get(0).isFail();
        }
        
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
        if(results.size() >0){
        	isFailed = isFailed || results.get(0).isFail();
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
    
    public boolean isStatisticsRequirement(){
    	boolean result = false;
    	result = result || ((group!=null) && (group.getName()!=null) &&  group.getName().indexOf("statistic")>=0);
    	result = result || ((subGroup!=null) && (subGroup.getName()!=null) &&  subGroup.getName().indexOf("statistic")>=0);
    	return result;
    }
}
