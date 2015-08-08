package com.miwashi.model;

import javax.persistence.*;
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
    private String since = "";

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

    public long getId() {
        return id;
    }

    public int getSuccesses() {
        return successes;
    }

    public void setSuccesses(int successes) {
        this.successes = successes;
    }

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    public int getUnstable() {
        return unstable;
    }

    public void setUnstable(int unstable) {
        this.unstable = unstable;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public int getTested() {
        return tested;
    }

    public void setTested(int tested) {
        this.tested = tested;
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

    public void add(Subject subject) {
        subjects.put(subject.getKey(), subject);
    }
}
