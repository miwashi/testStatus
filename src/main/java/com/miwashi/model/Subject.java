package com.miwashi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Subject {

    @Column(name = "SUBJECT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
		return id;
	}

	@Column(unique = true)
    private String key;

    private String name;

    @Transient
    private String group;

    @Transient
    private Map<String, RequirementRow> requirements = new HashMap<String,RequirementRow>();


    public Subject() {
        super();
    }

    public Subject(String name, String key) {
        super();
        this.name = name;
        this.key = key;
    }

    public class RequirementRow{
    	private long id; 
    	private String name;
        private int successRate;
        private String readableName;
        private boolean failed;
        private boolean succeeded;
        private boolean unstable;
        private int testsRun;

        public RequirementRow(String name){
            this.name = name;
        }

        public String toString(){
            return name;
        }

        public void setId(long id) {
			this.id = id;
		}
        
        public long getId() {
			return id;
		}
        
        public String getName(){
            return name;
        }

        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
        }

        public boolean isFailed() {
            return failed;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
        }

        public boolean isSucceeded() {
            return succeeded;
        }

        public void setSucceeded(boolean succeeded) {
            this.succeeded = succeeded;
        }

        public boolean isUnstable() {
            return unstable;
        }

        public void setUnstable(boolean unstable) {
            this.unstable = unstable;
        }

        public int getTestsRun() {
            return testsRun;
        }

        public void setTestsRun(int testsRun) {
            this.testsRun = testsRun;
        }

        public int getSuccessRate() {
            return successRate;
        }

        public void setSuccessRate(int successRate) {
            this.successRate = successRate;
        }
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<RequirementRow> getRequirements() {
        return requirements.values();
    }

    public void add(Requirement requirement){
        RequirementRow requirementRow = null;
        if(requirements.containsKey(requirement.getName())){
            requirementRow = requirements.get(requirement.getName());
        }else{
            requirementRow = new RequirementRow(requirement.getName());

            String readableRequirement = requirement.getTestRequirement();
            int numTests = (requirement.getResults()==null?0:requirement.getResults().size());
            requirementRow.setId(requirement.getId());
            requirementRow.setTestsRun(numTests);
            requirementRow.setUnstable(requirement.isUnstable());
            requirementRow.setFailed(requirement.isFailed());
            requirementRow.setUnstable(requirement.isUnstable());
            requirementRow.setSucceeded(requirement.isSuccess());
            requirementRow.setSuccessRate(requirement.getSuccessrate());

            String subject = requirement.getTestRequirement() + "!";
            subject = Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
            requirementRow.setReadableName(subject);

            requirements.put(requirement.getName(),requirementRow);
        }
    }
    
    public int getNumberOfRequirements(){
    	return requirements.size();
    }
    
    public int getNumberOfFailedRequirements(){
    	int numFails = 0;
    	for(RequirementRow row : requirements.values()){
    		if(row.failed){
    			numFails++;
    		}
    	}
    	return numFails;
    }
    
    public String getNumberOfFailedRequirementsShare(){
    	int numFails = 0;
    	for(RequirementRow row : requirements.values()){
    		if(row.failed){
    			numFails++;
    		}
    	}
    	return numFails + "%";
    }
    
    public int getNumberOfUnstableRequirements(){
    	int numUnstable = 0;
    	for(RequirementRow row : requirements.values()){
    		if(row.unstable){
    			numUnstable++;
    		}
    	}
    	return numUnstable;
    }
    
    public String getNumberOfUnstableRequirementsShare(){
    	int numUnstable = 0;
    	for(RequirementRow row : requirements.values()){
    		if(row.unstable){
    			numUnstable++;
    		}
    	}
    	return numUnstable + "%";
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
