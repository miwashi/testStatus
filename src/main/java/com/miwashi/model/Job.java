package com.miwashi.model;

import javax.persistence.*;

@Entity(name = "JOBS")
public class Job {

    @Column(name = "JOB_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(unique = true)
    private String key = "key";

    private String host = "";
    private String name = "";
    private String buildNumber = "";
    private String grid = "";
    private String gitCommit = "";
    private String gitURL = "";
    private String gitBranch = "";
    
    public Job(){
        super();
    }

    public Job(String key){
    	super();
        this.key = key;
    }

    public Job(String key, String name) {
    	super();
    	this.name = name;
    	this.key = key;
	}

	public String toString(){
		return id + " - " + key;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getGitCommit() {
		return gitCommit;
	}

	public void setGitCommit(String gitCommit) {
		this.gitCommit = gitCommit;
	}

	public String getGitURL() {
		return gitURL;
	}

	public void setGitURL(String gitURL) {
		this.gitURL = gitURL;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}
    
    
}
