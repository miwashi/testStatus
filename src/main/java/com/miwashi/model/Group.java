package com.miwashi.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity(name="GROUPS")
public class Group {

    @Column(name = "GROUP_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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

    public Collection<Subject> getSubjects() {
        return subjects.values();
    }

    public void add(Subject subject) {
        subjects.put(subject.getKey(), subject);
    }
}
