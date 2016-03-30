package net.miwashi.teststatus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@OrderBy("STARTTIME DESC")
    @JoinColumn(name = "JOB_ID", nullable = false)
    private List<CheckinComment> checkins = new ArrayList<CheckinComment>();

    private String host = "";
    private String name = "";
    private String buildNumber = "";
    private String grid = "";
    private String gitCommit = "";
    private String gitURL = "";
    private String gitBranch = "";
    
    private String jenkinsResult = "unknown";
    private long jenkinsDuration = 0;
    private int jenkinsTotalCount = 0;
    private int jenkinsFailCount = 0;
    private int jenkinsSkipCount = 0;
    private int jenkinsPassCount = 0;
    private String jenkinstTestReportUrl ="";
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
 
	public long getJenkinsDuration() {
		return jenkinsDuration;
	}

	public void setJenkinsDuration(long jenkinsDuration) {
		this.jenkinsDuration = jenkinsDuration;
	}
	
	public String getBuildUrl() {
		return ((buildUrl==null)?"":buildUrl + (buildUrl.endsWith("/")?"":"/"));
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
    
    public Job(JSONObject obj) {
    	super();
    	UUID uuid = UUID.randomUUID();
    	this.id = obj.getLong("id");
    	this.key = obj.getString("key");
    	this.name = obj.getString("name");
    	this.timeStamp = new Date(obj.getLong("timeStamp"));
    	this.host = obj.getString("host");
    	this.grid = obj.getString("grid");
    	this.gitBranch = obj.getString("host");
    	this.gitCommit = obj.getString("gitCommit");
    	this.gitBranch = obj.getString("gitBranch");
    	this.jenkinsResult = obj.getString("jenkinsResult");
    	this.jenkinsDuration = obj.getInt("jenkinsDuration");
    	this.jenkinsFailCount = obj.getInt("jenkinsFailCount");
    	this.jenkinsPassCount = obj.getInt("jenkinsPassCount");
    	this.jenkinsTotalCount = obj.getInt("jenkinsTotalCount");
    	this.jenkinsSkipCount = obj.getInt("jenkinsSkipCount");
    	this.jenkinsUrl = obj.getString("jenkinsUrl");
    	this.platform = obj.getString("platform");
    	this.size = obj.getString("size");
    	this.buildTag = obj.getString("buildTag");
    	this.browser = obj.getString("browser");
    	this.user = obj.getString("user");
    	this.buildUrl = obj.getString("buildUrl");
    	
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

	public List<CheckinComment> getCheckins() {
		return checkins;
	}

	public void setCheckins(List<CheckinComment> checkins) {
		this.checkins = checkins;
	}
	
	public void add(CheckinComment checkin) {
		this.checkins.add(checkin);
	}
}
