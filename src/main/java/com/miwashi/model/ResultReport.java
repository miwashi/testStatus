package com.miwashi.model;

import org.joda.time.DateTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.StringTokenizer;

/**
 * Created by miwa01 on 15-07-16.
 */
public class ResultReport {

    private String size;
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGitCommit() {
		return gitCommit;
	}

	public void setGitCommit(String gitCommit) {
		this.gitCommit = gitCommit;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getGitURL() {
		return gitURL;
	}

	public void setGitURL(String gitURL) {
		this.gitURL = gitURL;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	private String type;
    private String uuid;
    private String name;
    private String timeStamp;
    private String status;
    private String browser;
    private String version;
    private String platform;
    private String host;
    private String grid;
    private String user;
    private String buildId;
    private String buildNumber;
    private String jobName;
    private String gitCommit;
    private String gitBranch;
    private String gitURL;
    private String exceptionMessage;
    private DateTime completeTime = null;
    private DateTime startTime;

    public ResultReport(String msg){

        JsonObject json = (new JsonParser()).parse(msg).getAsJsonObject();
        if(!json.has("type")){
            throw new IllegalStateException("Missing type field");
        }
        if(!json.has("name")){
            throw new IllegalStateException("Missing name field");
        }

        StringTokenizer aTokenizer = new StringTokenizer(msg, ";");

        type = json.has("type")?json.get("type").getAsString():"";
        uuid = json.has("uuid")?json.get("uuid").getAsString():"";
        name = json.has("name")?json.get("name").getAsString():"";
        timeStamp = json.has("timeStamp")?json.get("timeStamp").getAsString():"";
        status = json.has("status")?json.get("status").getAsString():"";
        browser = json.has("browser")?json.get("browser").getAsString():"";
        version = json.has("version")?json.get("version").getAsString():"";
        platform = json.has("platform")?json.get("platform").getAsString():"";
        size = json.has("size")?json.get("size").getAsString():"";
        host = json.has("host")?json.get("host").getAsString():"";
        grid = json.has("grid")?json.get("grid").getAsString():"";
        user = json.has("user")?json.get("user").getAsString():"";
        buildId = json.has("buildId")?json.get("buildId").getAsString():"";
        buildNumber = json.has("buildNumber")?json.get("buildNumber").getAsString():"";
        jobName = json.has("jobName")?json.get("jobName").getAsString():"";
        gitCommit = json.has("gitCommit")?json.get("gitCommit").getAsString():"";
        gitBranch = json.has("gitBranch")?json.get("gitBranch").getAsString():"";
        gitURL = json.has("gitURL")?json.get("gitURL").getAsString():"";
        exceptionMessage = json.has("exceptionMessage")?json.get("exceptionMessage").getAsString():"";

        long time = Long.parseLong(timeStamp);
        startTime = new DateTime(time);
    }

    public String toString(){
        String result = "";

        long time = Long.parseLong(timeStamp);

        result += name;
        result += " " + status;
        result += " " + (new DateTime(time)).toString("yyyy hh:mm:ss",null);
        result += " " + timeStamp;
        result += "\n\t" + browser;
        result += "\n\t" + platform;
        result += "\n\t" + host;
        result += "\n\t" + grid;
        result += "\n\t" + user;
        result += "\n\t" + buildId;
        result += "\n\t" + buildNumber;
        result += "\n\t" + jobName;
        result += "\n\t" + gitCommit;
        result += "\n\t" + gitBranch;
        result += "\n\t" + gitURL;
        result += "\n\t" + exceptionMessage;

        return result;
    }

    public String getKey(){
        return name + "." + uuid;
    }

    public boolean isCompleted(){
        return "2".equalsIgnoreCase(type);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(DateTime completeTime) {
        this.completeTime = completeTime;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public String getBrowser() {
        return browser;
    }

    public String getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }
    
    public String getNameAsKey() {
        //return name==null?"":name.toLowerCase();
        return name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isFailed() {
        return "success".equalsIgnoreCase(status);
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
}
