package com.miwashi.model;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.*;

@Entity(name="GROUPS")
public class Group {

    @Column(name = "GROUP_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastTested;

    public Date getLastTested() {
    	return lastTested;
	}

	public void setLastTested(Date lastTested) {
		this.lastTested = lastTested;
	}

	@Transient
    private int tested = 0;

    @Column(unique = true)
    private String name = "name";

    @Transient
    private int successes = 0;

    @Transient
    private int fails = 0;

    @Transient
    private int unstable = 0;

    @Transient
    private String touched = "";

    @Transient
    private Map<String, Subject> subjects = new HashMap<String,Subject>();

    public Group(){

    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopName(){
    	String result = name;
    	if(name.indexOf('.')>=0){
    		result = name.substring(0, name.indexOf('.'));
    	}
    	return result;
    }
    
    public long getId() {
        return id;
    }

    public int getSuccesses() {
        return successes;
    }
    
    public String getSuccessesRelative() {
    	if(tested==0){
    		return "";
    	}
        return "" + successes + " (" + (Math.round((100 * successes)/tested)) + "%)";
    }

    public int getFails() {
        return fails;
    }
    
    public String getFailsRelative() {
    	if(tested==0){
    		return "";
    	}
        return "" + successes + " (" + (Math.round((100 * fails)/tested)) + "%)";
    }

    public int getUnstable() {
        return unstable;
    }
    
    public String getUnstableRelative() {
    	if(tested==0){
    		return "";
    	}
        return "" + successes + " (" + (Math.round((100 * unstable)/tested)) + "%)";
    }

    public String getTouched() {
    	if(lastTested == null){
    		return "not touched";
    	}
    	
    	DateTime now = DateTime.now();
    	DateTime thisTime = new DateTime(lastTested.getTime());
    	long duration = now.getMillis() - thisTime.getMillis();
    	if(duration < 0){
    		return "not touched";
    	}
    	long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
    	duration = duration - days * (1000 * 3600 * 24 );
    	long hours = Math.floorDiv(duration, (1000 * 3600 ));
    	duration = duration - days * (1000 * 3600);
    	long minutes = Math.floorDiv(duration, (1000 * 60 ));
    	duration = duration - days * (1000 * 60);
    	long seconds = Math.floorDiv(duration, (1000));
    	
    	if(days>0){
    		return days + " days ago!";
    	}
    	
    	if(hours>0){
    		return hours + " hour, " + ((hours>1)?"s":"") + " minutes ago!";
    	}
    	
    	if(minutes>0){
    		return minutes + " minute" + ((minutes>1)?"s":"") + " ago!";
    	}
    	
        return seconds + " seconds ago!";
    }
    
    public long getTouchedDays(){
    	if(lastTested == null){
    		return Integer.MAX_VALUE;
    	}
    	
    	DateTime now = DateTime.now();
    	DateTime thisTime = new DateTime(lastTested.getTime());
    	long duration = now.getMillis() - thisTime.getMillis();
    	if(duration < 0){
    		return Integer.MAX_VALUE;
    	}
    	long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
    	duration = duration - days * (1000 * 3600 * 24 );
    	long hours = Math.floorDiv(duration, (1000 * 3600 ));
    	duration = duration - days * (1000 * 3600);
    	long minutes = Math.floorDiv(duration, (1000 * 60 ));
    	duration = duration - days * (1000 * 60);
    	long seconds = Math.floorDiv(duration, (1000));
    	
    	return days;
    }
    
    public long getTouchedHours(){
    	if(lastTested == null){
    		return Integer.MAX_VALUE;
    	}
    	
    	DateTime now = DateTime.now();
    	DateTime thisTime = new DateTime(lastTested.getTime());
    	long duration = now.getMillis() - thisTime.getMillis();
    	if(duration < 0){
    		return Integer.MAX_VALUE;
    	}
    	long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
    	duration = duration - days * (1000 * 3600 * 24 );
    	long hours = Math.floorDiv(duration, (1000 * 3600 ));
    	duration = duration - days * (1000 * 3600);
    	long minutes = Math.floorDiv(duration, (1000 * 60 ));
    	duration = duration - days * (1000 * 60);
    	long seconds = Math.floorDiv(duration, (1000));
    	
    	return hours;
    }
    
    public long getTouchedMinutes(){
    	if(lastTested == null){
    		return Integer.MAX_VALUE;
    	}
    	
    	DateTime now = DateTime.now();
    	DateTime thisTime = new DateTime(lastTested.getTime());
    	long duration = now.getMillis() - thisTime.getMillis();
    	if(duration < 0){
    		return Integer.MAX_VALUE;
    	}
    	long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
    	duration = duration - days * (1000 * 3600 * 24 );
    	long hours = Math.floorDiv(duration, (1000 * 3600 ));
    	duration = duration - days * (1000 * 3600);
    	long minutes = Math.floorDiv(duration, (1000 * 60 ));
    	duration = duration - days * (1000 * 60);
    	long seconds = Math.floorDiv(duration, (1000));
    	
    	return minutes;
    }
    
    public long getTouchedSeconds(){
    	if(lastTested == null){
    		return Integer.MAX_VALUE;
    	}
    	
    	DateTime now = DateTime.now();
    	DateTime thisTime = new DateTime(lastTested.getTime());
    	long duration = now.getMillis() - thisTime.getMillis();
    	if(duration < 0){
    		return Integer.MAX_VALUE;
    	}
    	long days = Math.floorDiv(duration, (1000 * 3600 * 24 ));
    	duration = duration - days * (1000 * 3600 * 24 );
    	long hours = Math.floorDiv(duration, (1000 * 3600 ));
    	duration = duration - days * (1000 * 3600);
    	long minutes = Math.floorDiv(duration, (1000 * 60 ));
    	duration = duration - days * (1000 * 60);
    	long seconds = Math.floorDiv(duration, (1000));
    	
    	return seconds;
    }

    public int getTested() {
        return tested;
    }

    public List<Subject> getSubjects() {
        List<Subject> result = new ArrayList<Subject>();
        result.addAll(subjects.values());
        Collections.sort(result, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2) {
                if (subject1 == null || subject2 == null || subject1.getName() == null || subject2.getName() == null) {
                    return 0;
                }
                return subject1.getName().compareTo(subject2.getName());
            }
        });
        return result;
    }
    
    public int getNumberOfSubjects(){
    	return subjects==null?0:subjects.size();
    }
    
    public int getNumberOfFailedSubjects(){
    	int numFails=0;
    	for(Subject subject : subjects.values()){
    		if(subject.isFailed()){
    			numFails++;
    		}
    	}
    	return numFails;
    }
    
    public int getNumberOfUnstableSubjects(){
    	int numUnstable=0;
    	for(Subject subject : subjects.values()){
    		if(subject.isUnstable()){
    			numUnstable++;
    		}
    	}
    	return numUnstable;
    }
    
    public int getNumberOfRequirements(){
    	return subjects==null?0:subjects.size();
    }
    
    public int getNumberOfSucceededRequirements(){
    	return subjects==null?0:subjects.size();
    }
    
    public String getNumberOfSucceededRequirementsRatio(){
    	return "30%";
    }
    
    public int getNumberOfFailedRequirements(){
    	return subjects==null?0:subjects.size();
    }
    
    public String getNumberOfFailedRequirementsRatio(){
    	return "30%";
    }
    
    public int getNumberOfUnstableRequirements(){
    	return subjects==null?0:subjects.size();
    }
    
    public String getNumberOfUnstableRequirementsRatio(){
    	return "30%";
    }

    public void add(Subject subject) {
        subjects.put(subject.getKey(), subject);
    }
    
    public void resetStat(){
    	fails = 0;
    	successes = 0;
    	unstable = 0;
    	lastTested = null;
    }
    
    public void addStat(Requirement requirement) {
    	tested++;
    	
        if(requirement.isFailed()){
        	fails++;
        }
        
        if(requirement.isSuccess()){
        	successes++;
        }
        
        if(requirement.isUnstable()){
        	unstable++;
        }
        lastTested = requirement.getLastTested();
    }
}
