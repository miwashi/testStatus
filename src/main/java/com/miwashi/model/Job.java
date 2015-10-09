package com.miwashi.model;

import java.util.Date;

import javax.persistence.*;

@Entity(name = "JOBS")
public class Job {

    @Column(name = "JOB_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column(unique = true)
    private String key = "key";
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timeStamp;

    private String host = "";
    private String name = "";
    private String buildNumber = "";
    private String grid = "";
    private String gitCommit = "";
    private String gitURL = "";
    private String gitBranch = "";
    
    private String jenkinsResult = "unknown";
    private String jenkinsDuration = "unknown";
    private int jenkinsTotalCount = 0;
    private int jenkinsFailCount = 0;
    private int jenkinsSkipCount = 0;
    private int jenkinsPassCount = 0;
    private String jenkinstTestReportUrl ="";
    
    
    public String getJenkinsDuration() {
		return jenkinsDuration;
	}

	public void setJenkinsDuration(String jenkinsDuration) {
		this.jenkinsDuration = jenkinsDuration;
	}

	private String nodeName = "";
    private String jenkinsUrl = "";
    private String platform = "";
	private String size = "";
	private String buildTag = "";
	private String browser = "";
	private String buildUrl = "";
	private String user = "";
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getJenkinsUrl() {
		return jenkinsUrl;
	}

	public void setJenkinsUrl(String jenkinsUrl) {
		this.jenkinsUrl = jenkinsUrl;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBuildTag() {
		return buildTag;
	}

	public void setBuildTag(String buildTag) {
		this.buildTag = buildTag;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
 
	public String getBuildUrl() {
		return ((buildUrl==null)?"":buildUrl) + (buildUrl.endsWith("/")?"":"/");
	}
	
	//TODO Use properties
	public String getJobStatusUrl(){
		return getBuildUrl() + "api/json";
	}
	
	//TODO Use properties
	public String getResultReportUrl(){
		return getBuildUrl() + "testReport/api/json";
	}
	
	//TODO Use properties
	public String getLatestResultReportUrl(){
		return getBuildUrl()  + "lastBuild/api/json";
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getJenkinsResult() {
		return jenkinsResult;
	}

	public void setJenkinsResult(String jenkinsResult) {
		this.jenkinsResult = jenkinsResult;
	}
	
	

	public int getJenkinsTotalCount() {
		return jenkinsTotalCount;
	}

	public void setJenkinsTotalCount(int jenkinsTotalCount) {
		this.jenkinsTotalCount = jenkinsTotalCount;
	}

	public int getJenkinsFailCount() {
		return jenkinsFailCount;
	}

	public void setJenkinsFailCount(int jenkinsFailCount) {
		this.jenkinsFailCount = jenkinsFailCount;
	}
	
	public int getJenkinsFailShare() {
		return ((jenkinsTotalCount==0?0:Math.round((100 * jenkinsFailCount) / jenkinsTotalCount)));
	}

	public int getJenkinsSkipCount() {
		return jenkinsSkipCount;
	}

	public void setJenkinsSkipCount(int jenkinsSkipCount) {
		this.jenkinsSkipCount = jenkinsSkipCount;
	}

	
	
	public int getJenkinsPassCount() {
		return jenkinsPassCount;
	}

	public void setJenkinsPassCount(int jenkinsPassCount) {
		this.jenkinsPassCount = jenkinsPassCount;
	}

	public String getJenkinstTestReportUrl() {
		return jenkinstTestReportUrl;
	}

	public void setJenkinstTestReportUrl(String jenkinstTestReportUrl) {
		this.jenkinstTestReportUrl = jenkinstTestReportUrl;
	}

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
