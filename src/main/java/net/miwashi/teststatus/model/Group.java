package net.miwashi.teststatus.model;

import javax.persistence.*;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @Transient
    private Map<String, Requirement> requirements = new HashMap<String,Requirement>();

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

}
