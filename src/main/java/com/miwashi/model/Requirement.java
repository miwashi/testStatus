package com.miwashi.model;

import com.miwashi.config.ApplicationConfig;
import com.miwashi.repositories.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name="REQUIREMENTS")
public class Requirement {

    @Column(name = "REQUIREMENT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String name = "name";

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "REQURIEMENT_ID", nullable = false)
    private Collection<Result> results = new ArrayList<Result>();

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

    public Requirement(String name){
        super();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Result> getResults() {
        return results;
    }

    public void setResults(Collection<Result> results) {
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
        if(name.indexOf(".")<0){
            return name;
        }
        testRequiement = name.substring(name.lastIndexOf("."));
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
