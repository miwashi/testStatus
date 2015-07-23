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

    public String getTestSubjectKey(){
        String testSubject = name;
        if(testSubject.indexOf(".")<0){
            return testSubject;
        }
        testSubject = testSubject.substring(0, testSubject.lastIndexOf("."));
        testSubject = testSubject.toLowerCase();
        return testSubject;
    }

    public String getTestSubject(){
        String testSubject = "";
        if(name.indexOf(".")<0){
            return name;
        }
        testSubject = name.substring(0, name.lastIndexOf("."));
        if(testSubject.indexOf(".")>0) {
            testSubject = testSubject.substring(testSubject.lastIndexOf("."));
        }
        testSubject = testSubject.replace("Test","");
        testSubject = testSubject.replaceAll("\\.", "");
        testSubject = testSubject.replaceAll("(.)([A-Z])", "$1 $2");
        testSubject = testSubject.toLowerCase();
        return testSubject;
    }

    public String getTestGroup(){
        String testGroup = "";
        if(name.indexOf(".")<0){
            return name;
        }
        testGroup = name;
        testGroup = testGroup.replace("se.svt.test.","");
        if(testGroup.indexOf(".")>0){
            testGroup = testGroup.substring(0, testGroup.indexOf("."));
        }
        return testGroup;
    }

    public String getTestSubGroup(){
        String testGroup = "";
        if(name.indexOf(".")<0){
            return name;
        }
        testGroup = name;
        testGroup = testGroup.replace("se.svt.test.","");
        if(testGroup.indexOf(".")>0){
            testGroup = testGroup.substring(0, testGroup.lastIndexOf("."));
        }
        if(testGroup.indexOf(".")>0){
            testGroup = testGroup.substring(0, testGroup.lastIndexOf("."));
        }
        return testGroup;
    }

    public Date lastTested(){
        if(lastResult==null){
            return null;
        }
        return lastResult.getStartTime();
    }

    public String lastTestedStr(){
        if((lastResult==null) || (lastResult.getStartTime()==null)){
            return "n/a";
        }
        return ApplicationConfig.TIME_STAMP.format(lastResult.getStartTime());
    }
}
